// 시간 포맷팅 함수
function formatTime(date, time) {
	// 24시간제에서 12시간제로 변환
	let hour = parseInt(time.slice(0, 2)); // 시간 (00 ~ 23)
	let minute = time.substring(2, 4); // 분 (00 ~ 59)

	// 오전/오후 구분
	let ampm = hour >= 12 ? "오후" : "오전";

	// 12시간제로 변환 (12시, 00시는 12시로 변환)
	let formattedHour = hour % 12;
	formattedHour = formattedHour === 0 ? 12 : formattedHour; // 0시 -> 12시로 처리

	// "오전/오후 HH:MM" 형식으로 시간 포맷
	let formattedTime = `${ampm} ${formattedHour}:${minute}`;

	// 날짜 포맷 (YYYY-MM-DD)
	let formattedDate = `${date.slice(0, 4)}-${date.slice(4, 6)}-${date.slice(6, 8)}`;

	return { date: formattedDate, time: formattedTime };
}

// 날씨 상태에 따른 이미지 반환 (날 vs 밤 구분)
function getWeatherImage(sky, fcstTime) {
	let isNight = (parseInt(fcstTime) >= 1800 || (fcstTime >= "0000" && fcstTime < "0600")); 
	// fcstTime이 1800 이상이면 밤
	switch (sky) {
		case "맑음":
			return isNight ? "images/weather/맑음밤.gif" : "images/weather/맑음.gif";
		case "구름 많음":
			return isNight ? "images/weather/구름많음.gif" : "images/weather/구름많음.gif";
		case "흐림":
			return isNight ? "images/weather/흐림밤.gif" : "images/weather/흐림아침.gif";
		case "비":
			return "images/weather/비.gif";
		case "눈":
			return "images/weather/함박눈.gif";
		default:
			return "images/weather/default.gif";
	}
}

// 풍향에 따른 이미지 반환
function getWindDirectionImage(direction) {
	if (!direction) return { image: "images/weather/value/ARROW.png", rotation: 0 };
	let deg = parseFloat(direction);
	return { image: "images/weather/value/ARROW.png", rotation: deg };
}

// 일출 이미지
function getWeatherSunriseImg() {
	return "images/weather/일출.gif"
}

// 일몰 이미지
function getWeatherSunsetImg() {
	return "images/weather/일몰.gif";
}

function code_value(category, code) {
	let value = "-";
	if (code) {
		if (category === "SKY") {
			if (code === "1") value = "맑음";
			else if (code === "3") value = "구름 많음";
			else if (code === "4") value = "흐림";
		} else if (category === "PTY") {
			if (code === "0") value = "없음";
			else if (code === "1") value = "비";
			else if (code === "2") value = "비/눈";
			else if (code === "3") value = "눈";
			else if (code === "5") value = "빗방울";
			else if (code === "6") value = "빗방울눈날림";
			else if (code === "7") value = "눈날림";
		}
	}
	return value;
}

// API 제공 시간을 정확히 맞추는 함수
	function getBaseTime(hours, minutes) {
		// 제공되는 시간: 02:10, 05:10, 08:10, 11:10, 14:10, 17:10, 20:10, 23:10
		var apiTimes = ["0200", "0500", "0800", "1100", "1400", "1700", "2000", "2300"];

		// 현재 시간을 "HH:MM" 형식으로 구하기
		var currentTime = `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}`;

		// 현재 시간보다 이전의 가장 가까운 시간 찾기
		for (var i = apiTimes.length - 1; i >= 0; i--) {
			if (currentTime >= apiTimes[i]) {
				return apiTimes[i];  // 가장 가까운 시간 반환
			}
		}

		// 만약 현재 시간이 가장 늦은 시간보다 빠르다면 마지막 시간 반환
		return apiTimes[apiTimes.length - 1]; // 마지막 시간 23:10
	}

$(function() {
	$(document).ready(function() {
		// 로딩 스피너 HTML 추가
		const loaderHtml = `
			<div id="loader" style="display: none;">
				<div class="spinner"></div>
				<p>데이터를 로드 중입니다...</p>
			</div>
		`;
		$("body").append(loaderHtml); // HTML에 로딩 스피너 추가

		// 로딩 스피너 CSS 추가
		$("<style>")
			.prop("type", "text/css")
			.html(`
				#loader {
					position: fixed;
					top: 50%;
					left: 50%;
					transform: translate(-50%, -50%);
					z-index: 1000;
					text-align: center;
					background: rgba(255, 255, 255, 0.8);
					padding: 20px;
					border-radius: 10px;
					box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
				}
				#loader .spinner {
					width: 50px;
					height: 50px;
					border: 5px solid #ddd;
					border-top: 5px solid #007bff;
					border-radius: 50%;
					animation: spin 1s linear infinite;
					margin: 0 auto 10px auto;
				}
				@keyframes spin {
					from {
						transform: rotate(0deg);
					}
					to {
						transform: rotate(360deg);
					}
				}
			`)
			.appendTo("head");

		$("#btn_weather").click(function() {
			// 입력 값 유효성 검사
			if ($("#nx").val() === "" || $("#ny").val() === "" || $("#baseDate").val() === "" || $("#baseTime").val() === "") {
				alert("현재 위치를 입력 해주세요.");
				return;
			}

			// 로딩 스피너 표시
			$("#loader").show();

			// 파라미터 설정
			let serviceKey = "Gow%2FB%2BpvwKtRdRGfWEsPYdmR4X8u8LB342Dka9AaCg6XgZaYHeeOBcWH8aK9VT%2BfYSDLtu0o9k6WY%2BRp7E00ZA%3D%3D";
			let nx = $("#nx").val();
			let ny = $("#ny").val();
			let baseDate = $("#baseDate").val();
			let baseTime = $("#baseTime").val();
			let pageNo = 1;
			let numOfRows = 579;
			let dataType = "JSON";

			// 경도와 위도 값 정수형으로 변환
			let longitudeNum = Math.floor(parseFloat($("#longitudeNum").val()));  // 경도
			let latitudeNum = Math.floor(parseFloat($("#latitudeNum").val()));   // 위도

			// 기상청 API URL 동적 생성
			//let apiUrl = `http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=${serviceKey}&pageNo=${pageNo}&numOfRows=${numOfRows}&dataType=${dataType}&base_date=${baseDate}&base_time=${baseTime}&nx=${nx}&ny=${ny}`;
			let apiUrl = `https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=${serviceKey}&pageNo=${pageNo}&numOfRows=${numOfRows}&dataType=${dataType}&base_date=${baseDate}&base_time=${baseTime}&nx=${nx}&ny=${ny}`;
			//console.log("API 호출 URL:", apiUrl);

			// 일출/일몰 정보 API 호출 (위도, 경도를 이용)
			//let sunriseSunsetApiUrl = `http://apis.data.go.kr/B090041/openapi/service/RiseSetInfoService/getLCRiseSetInfo?longitude=${longitudeNum}&latitude=${latitudeNum}&locdate=${baseDate}&dnYn=N&ServiceKey=${serviceKey}`;
			let sunriseSunsetApiUrl = `https://apis.data.go.kr/B090041/openapi/service/RiseSetInfoService/getLCRiseSetInfo?longitude=${longitudeNum}&latitude=${latitudeNum}&locdate=${baseDate}&dnYn=N&ServiceKey=${serviceKey}`;
			//console.log("일출/일몰 정보 API 호출 URL:", sunriseSunsetApiUrl);

			// 일출/일몰 API 호출
			$.ajax({
				url: sunriseSunsetApiUrl,
				type: "GET",
				dataType: "json",
				success: function(response) {
					console.log("일출/일몰 정보 응답:", response);

					// sunrise와 sunset 값 초기화
					let sunrise = "";
					let sunset = "";

					// 응답에서 일출, 일몰 정보 추출
					if (response.response.header.resultCode === "00" && response.response.body.items.item) {
						sunrise = response.response.body.items.item.sunrise.trim(); // 일출 시간
						sunset = response.response.body.items.item.sunset.trim(); // 일몰 시간
					}

					// 값이 없으면 알림 표시
					if (!sunrise || !sunset) {
						alert("일출/일몰 정보가 없거나 잘못된 응답이 왔습니다.");
					}

					// 12시간제로 변환 (오전/오후 포함)
					let formattedSunrise = formatTime('', sunrise); // 일출 시간 포맷
					let formattedSunset = formatTime('', sunset);   // 일몰 시간 포맷

					// 일출과 일몰 시간 표시
					$('#sunrise').text(formattedSunrise.time ? `일출: ${formattedSunrise.time}` : "일출 정보 없음");
					$('#sunset').text(formattedSunset.time ? `일몰: ${formattedSunset.time}` : "일몰 정보 없음");

					// 날씨 데이터 호출
					$.ajax({
						url: apiUrl,
						type: "GET",
						dataType: "json",
						success: function(response) {
							console.log("API 응답:", response);

							if (response.response.header.resultCode === "00") {
								let items = response.response.body.items.item;
								console.log("응답 받은 날씨 데이터:", items);

								let resultTable = $("#resultTable tbody");
								resultTable.empty(); // 기존 테이블 초기화

								let weatherDataByTime = {};

								// 현재 시간 계산
								let now = new Date();
								let currentDate = `${now.getFullYear()}${String(now.getMonth() + 1).padStart(2, "0")}${String(now.getDate()).padStart(2, "0")}`;
								let currentHour = now.getHours(); // 현재 시간만 가져옵니다.

								// 일몰시간 계산 (예: 18:00을 기준으로 설정)
								let sunsetTime = 18; // 일몰시간을 18:00으로 고정
								//let sunsetTime = formattedSunset; // 일몰시간을 18:00으로 고정

								// isDayTime 정의
								let isDayTime = currentHour >= 6 && currentHour < sunsetTime; // 6시부터 18시까지를 낮 시간으로 설정
								//let isDayTime = currentHour >= formattedSunrise && currentHour < sunsetTime; // 6시부터 18시까지를 낮 시간으로 설정

								// 시간별 데이터 정리
								items.forEach(function(item) {
									let timeKey = `${item.fcstDate} ${item.fcstTime}`;
									if (!weatherDataByTime[timeKey]) {
										weatherDataByTime[timeKey] = {
											date: item.fcstDate,
											time: item.fcstTime,
											sky: "-",          // 하늘 상태 (예: 맑음, 구름 많음, 흐림 등)
											pty: "-",          // 강수 형태 (예: 없음, 비, 눈, 비/눈, 빗방울 등)
											temp: "-",         // 기온 (예: 20℃)
											humidity: "-",     // 습도 (예: 50%)
											lgt: "-",          // 적설량 (예: 0cm)
											vvv: "-",          // 남북풍 (예: -1.2m/s)
											uuu: "-",          // 동서풍 (예: 1.5m/s)
											pcp: "-",          // 강수량 (예: 0mm)
											vec: "-",          // 풍향 (예: 270deg, 0deg 등)
											wsd: "-",          // 풍속 (예: 5m/s)
											sno: "적설없음"           // 적설량 (예: 0cm)
										};
									}

									// 각 카테고리별 데이터 처리
									if (item.category === "TMP") {
										weatherDataByTime[timeKey].temp = item.fcstValue || "-";
									}
									if (item.category === "SKY") {
										weatherDataByTime[timeKey].sky = item.fcstValue ? code_value("SKY", item.fcstValue) : "-";
									}
									if (item.category === "PTY") {
										weatherDataByTime[timeKey].pty = item.fcstValue || "-";
									}
									if (item.category === "REH") {
										weatherDataByTime[timeKey].humidity = item.fcstValue ? `${item.fcstValue}%` : "-";
									}
									if (item.category === "VEC") {
										weatherDataByTime[timeKey].vec = item.fcstValue ? `${item.fcstValue}deg` : "-";
									}
									if (item.category === "WSD") {
										weatherDataByTime[timeKey].wsd = item.fcstValue ? `${item.fcstValue}m/s` : "-";
									}
									if (item.category === "UUU") {
										weatherDataByTime[timeKey].uuu = item.fcstValue ? `${item.fcstValue}m/s` : "-";
									}
									if (item.category === "VVV") {
										weatherDataByTime[timeKey].vvv = item.fcstValue ? `${item.fcstValue}m/s` : "-";
									}
									if (item.category === "PCP") {
										weatherDataByTime[timeKey].pcp = item.fcstValue || "-";
									}
									if (item.category === "SNO") {
										weatherDataByTime[timeKey].sno = item.fcstValue ? `${item.fcstValue}` : "적설없음";
									}
								});

								let count = 0;
								for (let time in weatherDataByTime) {
									let weather = weatherDataByTime[time];

									if (count >= 48) break;

									let formatted = formatTime(weather.date, weather.time);
									let row = $("<tr></tr>");
									row.append(`<td>${formatted.date}</td>`);
									row.append(`<td>${formatted.time}</td>`);

									// sky 상태가 '맑음'일 때 낮/밤 구분 추가

									let skyLabel = weather.sky;

									if (skyLabel === "맑음") {
									    if (weather.time >= 1800 || (weather.time >= 0 && weather.time < 600)) {
									        skyLabel = "맑음(밤)";
									    } else {
									        skyLabel = "맑음(낮)";
									    }
									}


									let weatherImg = getWeatherImage(weather.sky, weather.time);
									row.append(`<td><img src="${weatherImg}" alt="weather icon" style="width: 50px; height: 50px; text-align: center;"/><br>${skyLabel}</td>`);
									row.append(`<td>${weather.temp}℃</td>`);
									row.append(`<td>${weather.pty}%</td>`);
									row.append(`<td>🌧️${weather.pcp}<br><br>❄️${weather.sno}</td>`);
									row.append(`<td>${weather.humidity}</td>`);
									let windImgData = getWindDirectionImage(weather.vec);
									row.append(
										`<td><img src="${windImgData.image}" alt="wind direction" style="width: 50px; height: 50px; transform: rotate(${windImgData.rotation}deg); transform-origin: center;" /><br>${weather.wsd}</td>`
									);
									row.append(`<td>${weather.uuu}<br>${weather.vvv}</td>`);

									// 일출과 일몰 이미지 추가
									let weatherSunriseImg = getWeatherSunriseImg();
									let weatherSunsetImg = getWeatherSunsetImg();
									row.append(
										`
									     <td><img src="${weatherSunriseImg}" alt="weather icon" style="width: 50px; height: 50px; text-align: center;"/><br>${formattedSunrise.time}</td>
									     <td><img src="${weatherSunsetImg}" alt="weather icon" style="width: 50px; height: 50px; text-align: center;"/><br>${formattedSunset.time}</td>`
									); // 12시간제로 변환된 일출/일몰 시간 추가
									resultTable.append(row);
									count++;
								}

								$("#resultTable").show();
							} else {
								alert("예보 데이터가 없습니다.");
							}
						},
						error: function(error) {
							console.error("API 호출 오류:", error);
							alert("API 호출 중 오류가 발생했습니다.");
						},
						complete: function() {
							// 로딩 스피너 숨기기
							$("#loader").hide();
						}
					});
				},
				error: function(error) {
					console.error("일출/일몰 API 호출 오류:", error);
					alert("일출/일몰 API 호출 중 오류가 발생했습니다.");
				},
				complete: function() {
					// 로딩 스피너 숨기기
					$("#loader").hide();
				}
			});
		});
	});
});