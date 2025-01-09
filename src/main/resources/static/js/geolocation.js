// DOM이 준비되면 실행될 콜백 함수
$(function () {
    function getLocationAndSubmit() {
        if (navigator.geolocation) {
            getPreciseLocation((preciseLocation) => {
                const latitudeNum = preciseLocation.latitude;
                const longitudeNum = preciseLocation.longitude;

                // 좌표를 주소로 변환 (카카오 API 사용)
                const geocoder = new kakao.maps.services.Geocoder();
                geocoder.coord2RegionCode(longitudeNum, latitudeNum, (regionResult, status) => {
                    if (status === kakao.maps.services.Status.OK) {
                        // 시/도 및 구/군 정보 추출
                        const city = regionResult[0].region_1depth_name || ""; // 시/도
                        const district = regionResult[0].region_2depth_name || ""; // 구/군
                        const fullAddress = `${city} ${district}`; // 전체 주소 조합

                        // 지역 매핑 객체에서 regId와 regIdTemp 가져오기
                        const fullCity = regionMapping[city] || city; // 매핑된 시/도 이름
                        const cityData = locations[fullCity];

                        if (!cityData) {
                            console.error("해당 지역 데이터를 찾을 수 없습니다:", fullCity);
                            return;
                        }

                        const regId = cityData.reg_code?.regId || "Unknown";
                        const regIdTemp = cityData.reg_code?.regIdTemp || "Unknown";

                        // 기상청의 좌표 변환 함수 적용 (위경도 -> 격자 좌표)
                        const gridResult = dfs_xy_conv("toXY", latitudeNum, longitudeNum);

                        // 변환된 격자 좌표
                        const nx = gridResult.x;
                        const ny = gridResult.y;

                        // 필드 업데이트
                        $("#localaddrese").val(fullAddress); // 현재 위치 주소
                        $("#nx").val(nx); // 격자 좌표 x
                        $("#ny").val(ny); // 격자 좌표 y
                        $("#latitudeNum").val(latitudeNum); // 위도
                        $("#longitudeNum").val(longitudeNum); // 경도
                        $("#regId").val(regId); // regId
                        $("#regIdTemp").val(regIdTemp); // regIdTemp

                        // 로그 확인 (디버깅용)
                        console.log("도시:", fullCity);
                        console.log("구/군:", district);
                        console.log("regId:", regId);
                        console.log("regIdTemp:", regIdTemp);
                        console.log("현재 위치 주소:", fullAddress);

                        // 서버로 데이터 전송 (이 부분을 여기로 이동)
                        // 단일로 사용할때 사용 통합버전은 주석 처리
                        /*sendWeatherData(nx, ny);*/

                    } else {
                        console.error("주소 변환 실패");
                        alert("주소를 가져오지 못했습니다.");
                    }
                });
            });
        } else {
            alert("이 브라우저에서는 위치정보가 지원되지 않습니다.");
        }
    }

    // 서버로 데이터 전송 함수
    // 단일로 사용할때 사용 통합버전은 주석 처리
    /*function sendWeatherData(nx, ny) {
        const weatherDto = {
            baseDate: $("#baseDate").val(),
            baseTime: $("#baseTime").val(),
            nx: nx,
            ny: ny,
        };

        fetch('/getWeather', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(weatherDto),
        })
            .then((response) => {
                if (!response.ok) {
                    return response.text().then((text) => {
                        console.error("서버 오류:", text);
                        throw new Error('날씨 데이터를 가져오지 못했습니다.');
                    });
                }
                return response.json();
            })
            .then((data) => {
                console.log("날씨 데이터: ", data);
            })
            .catch((error) => {
                console.error("에러:", error);
            });
    }*/

    // 버튼 클릭 이벤트 리스너 등록
    $("#getWeatherButton").on("click", getLocationAndSubmit);
});

// 정확한 위치를 얻기 위한 함수
function getPreciseLocation(callback) {
    navigator.geolocation.getCurrentPosition(
        (position) => {
            const accuracy = position.coords.accuracy;
            const latitude = position.coords.latitude;
            const longitude = position.coords.longitude;

            console.log(`Position received: Lat=${latitude}, Lon=${longitude}, Accuracy=${accuracy}m`);
            callback({ latitude, longitude });
        },
        (error) => {
            console.error("위치 정보 오류:", error);
            alert("위치 정보를 가져오는 데 실패했습니다.");
        },
        { enableHighAccuracy: true, timeout: 10000, maximumAge: 0 }
    );
}

// 기상청 좌표 변환 함수 (위경도 -> 격자 좌표 변환)
function dfs_xy_conv(code, v1, v2) {
    var RE = 6371.00877; // 지구 반경(km)
    var GRID = 5.0; // 격자 간격(km)
    var SLAT1 = 30.0; // 투영 위도1(degree)
    var SLAT2 = 60.0; // 투영 위도2(degree)
    var OLON = 126.0; // 기준점 경도(degree)
    var OLAT = 38.0; // 기준점 위도(degree)
    var XO = 43; // 기준점 X좌표(GRID)
    var YO = 136; // 기준점 Y좌표(GRID)

    var DEGRAD = Math.PI / 180.0;
    var RADDEG = 180.0 / Math.PI;

    var re = RE / GRID;
    var slat1 = SLAT1 * DEGRAD;
    var slat2 = SLAT2 * DEGRAD;
    var olon = OLON * DEGRAD;
    var olat = OLAT * DEGRAD;

    var sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
    sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
    var sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
    sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
    var ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
    ro = re * sf / Math.pow(ro, sn);

    var rs = {};
    if (code === "toXY") {
        var ra = Math.tan(Math.PI * 0.25 + (v1) * DEGRAD * 0.5);
        ra = re * sf / Math.pow(ra, sn);
        var theta = v2 * DEGRAD - olon;
        if (theta > Math.PI) theta -= 2.0 * Math.PI;
        if (theta < -Math.PI) theta += 2.0 * Math.PI;
        theta *= sn;
        rs.x = Math.floor(ra * Math.sin(theta) + XO + 0.5);
        rs.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
    } else {
        rs.x = v1;
        rs.y = v2;
        var xn = v1 - XO;
        var yn = ro - v2 + YO;
        var ra = Math.sqrt(xn * xn + yn * yn);
        if (sn < 0.0) {
            ra = -ra;
        }
        var alat = Math.pow((re * sf / ra), (1.0 / sn));
        alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

        var theta = 0.0;
        if (Math.abs(xn) <= 0.0) {
            theta = 0.0;
        } else {
            if (Math.abs(yn) <= 0.0) {
                theta = Math.PI * 0.5;
                if (xn < 0.0) {
                    theta = -theta;
                }
            } else theta = Math.atan2(xn, yn);
        }
        var alon = theta / sn + olon;
        rs.lat = alat * RADDEG;
        rs.lng = alon * RADDEG;
    }
    return rs;
}