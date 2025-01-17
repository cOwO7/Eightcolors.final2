document.addEventListener("DOMContentLoaded", async () => {
    const weatherContainer = document.getElementById("weatherContainer");

    console.log('여기까지 이상 무');

    // 스크롤 좌/우 이동
    const row2 = document.getElementById("row2");
    window.scrollToLeft = () => row2.scrollBy({left: -300, behavior: "smooth"});
    window.scrollToRight = () => row2.scrollBy({left: 300, behavior: "smooth"});

    // 스크롤 버튼 hover 이벤트
    const weatherScrollContainer = document.querySelector(".weather-scroll-container");
    const scrollButtons = document.querySelectorAll(".scroll-btn");
    weatherScrollContainer.addEventListener("mouseenter", () => toggleScrollButtonsOpacity(1));
    weatherScrollContainer.addEventListener("mouseleave", () => toggleScrollButtonsOpacity(0));

    function toggleScrollButtonsOpacity(opacity) {
        scrollButtons.forEach(button => button.style.opacity = opacity);
    }

    // 로딩 스피너 HTML 및 스타일 추가
    addLoader();

    function addLoader() {
        const loaderHtml = `
            <div id="loader" style="display: block;">
                <div class="spinner"></div>
                <p>데이터를 로드 중입니다...</p>
            </div>
        `;
        document.body.insertAdjacentHTML("beforeend", loaderHtml);

        const loaderStyle = document.createElement("style");
        loaderStyle.innerHTML = `
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
                from { transform: rotate(0deg); }
                to { transform: rotate(360deg); }
            }
        `;
        document.head.appendChild(loaderStyle);
    }

    function getBaseTime(hours, minutes) {
        const apiTimes = ["0200", "0500", "0800", "1100", "1400", "1700", "2000", "2300"];
        const currentTime = `${String(hours).padStart(2, '0')}${String(minutes).padStart(2, '0')}`;

        // 현재 시간에 맞는 baseTime을 찾아서 반환
        for (let i = apiTimes.length - 1; i >= 0; i--) {
            if (currentTime >= apiTimes[i]) {
                return apiTimes[i];
            }
        }
        return apiTimes[apiTimes.length - 1];  // 기본적으로 2300 반환
    }

    function setBaseDateAndTime() {
        const now = new Date();
        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, '0');
        const date = String(now.getDate()).padStart(2, '0');
        const hours = now.getHours();
        const minutes = now.getMinutes();

        // getBaseTime을 사용하여 baseTime 구하기
        const baseTime = getBaseTime(hours, minutes);
        const baseDate = `${year}${month}${date}`;

        const baseDateInput = document.getElementById("baseDate");
        const baseTimeInput = document.getElementById("baseTime");

        if (baseDateInput) baseDateInput.value = baseDate;
        if (baseTimeInput) baseTimeInput.value = baseTime;
    }

    // tmFc 값은 그대로 사용하므로, 그 부분만 기존 코드대로 설정
    const {forecastDate, forecastTime} = getForecastDateAndTime();
    const tmFcInput = document.getElementById("tmFc");
    if (tmFcInput) tmFcInput.value = forecastDate + forecastTime;

    // 페이지 로드 시 baseDate와 baseTime을 설정
    setBaseDateAndTime();

    // 기존에 tmFc를 설정하는 getForecastDateAndTime 함수
    function getForecastDateAndTime() {
        const now = new Date();
        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, '0');
        const date = String(now.getDate()).padStart(2, '0');
        const hours = now.getHours();

        let forecastTime = "1800";
        if (hours >= 6 && hours < 18) forecastTime = "0600";

        const forecastDate = `${year}${month}${date}`;
        return {forecastDate, forecastTime};
    }


    // 강수 확률 값 정리 함수
    function cleanRainProbability(value) {
        return value && value.includes("/") ? value.split("/")[0].trim() : value || '0%';
    }

    // TMN과 TMX 값 보완 함수
    function calculateMissingTMNAndTMX(combinedData) {
        Object.entries(combinedData).forEach(([dateKey, details]) => {
            if (!details.TMN && details.TMP) {
                details._TMP_LIST = details._TMP_LIST || [];
                details._TMP_LIST.push(parseFloat(details.TMP));
            }
            if (details._TMP_LIST) {
                const temps = details._TMP_LIST;
                details.TMN = Math.min(...temps).toFixed(1);
                details.TMX = Math.max(...temps).toFixed(1);
                delete details._TMP_LIST;
            }
        });
    }

    // SKY 값을 문자열로 변환하는 함수
    function code_value(category, code, weatherForecast) {
        const skyCodes = {"1": "맑음", "3": "구름 많음", "4": "흐림"};
        return category === "SKY" && code ? skyCodes[code] || "-" : weatherForecast || "-";
    }

    // 날씨 이미지를 가져오는 함수
    function getWeatherImage(sky, fcstTime, weatherForecast) {
        const isNight = parseInt(fcstTime) >= 1800 || (fcstTime >= "0000" && fcstTime < "0600");

        // 'sky'가 제공된 경우
        if (sky) {
            if (sky.includes("맑음")) {
                return isNight ? "/images/weather/맑음밤.gif" : "/images/weather/맑음.gif";
            } else if (sky.includes("구름")) {
                return "/images/weather/구름많음.gif";
            } else if (sky.includes("흐림")) {
                return isNight ? "/images/weather/흐림밤.gif" : "/images/weather/흐림아침.gif";
            } else if (sky.includes("비")) {
                return "/images/weather/비.gif";
            } else if (sky.includes("눈")) {
                return "/images/weather/함박눈.gif";
            }
        }

        // 'weatherForecast'가 제공된 경우 (sky 값이 없을 경우)
        if (weatherForecast) {
            if (weatherForecast.includes("맑음")) {
                return isNight ? "/images/weather/맑음밤.gif" : "/images/weather/맑음.gif";
            } else if (weatherForecast.includes("구름")) {
                return "/images/weather/구름많음.gif";
            } else if (weatherForecast.includes("흐림")) {
                return isNight ? "/images/weather/흐림밤.gif" : "/images/weather/흐림아침.gif";
            } else if (weatherForecast.includes("비")) {
                return "/images/weather/비.gif";
            } else if (weatherForecast.includes("눈")) {
                return "/images/weather/함박눈.gif";
            }
        }

        // 기본 값 반환
        return "/images/weather/default.gif";
    }


    // 풍향 이미지 가져오기
    function getWindDirectionImage(direction) {
        if (!direction) return {image: "/images/weather/value/ARROW.png", rotation: 0};
        let deg = parseFloat(direction);
        return {image: "/images/weather/value/ARROW.png", rotation: deg};
    }

    // 날씨 데이터를 카드로 렌더링
    function renderWeatherCards(combinedData) {
        const row1 = document.getElementById("row1");
        const row2 = document.getElementById("row2");

        const today = new Date();
        const daysOfWeek = ['일', '월', '화', '수', '목', '금', '토'];
        const options = {month: "numeric", day: "numeric"};

        const weatherData = Object.entries(combinedData).map(([dateKey, details], index) => {
            const futureDate = new Date(today);
            futureDate.setDate(today.getDate() + index);
            const dayOfWeek = daysOfWeek[futureDate.getDay()];

            const skyValue = code_value("SKY", details.SKY, details.weatherForecast);
            const morningRain = cleanRainProbability(details.rainMorning || details.POP || details.rainProbability || '0%');
            const afternoonRain = cleanRainProbability(details.rainAfternoon || details.POP || details.rainProbability || '0%');
            const morningIcon = getWeatherImage(skyValue, '0600', details.weatherForecast);
            const afternoonIcon = getWeatherImage(skyValue, '1800', details.weatherForecast);

            return {
                day: index === 0 ? "오늘" : index === 1 ? "내일" : `${dayOfWeek}`,
                date: futureDate.toLocaleDateString("ko-KR", options),
                morningRain,
                afternoonRain,
                morningIcon,
                afternoonIcon,
                tempMorning: `${details.TMN || details.minTemperature || '--'}°`,
                tempAfternoon: `${details.TMX || details.maxTemperature || '--'}°`,
                WSD: details.WSD || "--",
                VEC: details.VEC || "--",
                REH: details.REH || "--"
            };
        });

        weatherData.forEach((data, index) => {
            const card = document.createElement("div");
            card.className = "day-card";
            if (index === 0) card.classList.add("today");
            if (index === 1) card.classList.add("tomorrow");

            card.innerHTML = `
                <h3>${data.day}</h3>
                <p>${data.date}</p>
                <div class="weather">
                    <div class="morning">
                        <span>오전</span>
                        <img src="${data.morningIcon}" alt="Morning Weather Icon" class="icon">
                        <span class="on">🌡️: ${data.tempMorning}</span><br/>
                        <span>강수 확률🌧: ${data.morningRain}%</span>
                    </div>
                    <div class="afternoon">
                        <span>오후</span>
                        <img src="${data.afternoonIcon}" alt="Afternoon Weather Icon" class="icon">
                        <span class="on2">🌡️: ${data.tempAfternoon}</span><br/>
                        <span>강수 확률🌧: ${data.afternoonRain}%</span>
                    </div>
                </div>
            `;

            if (index <= 0) {
                card.classList.add("today");
                const windDirectionData = getWindDirectionImage(data.VEC);
                card.innerHTML = `
                    <div class="weather-today">
                        <div class="morning-str">
                            <span class="cq">오전</span>
                            <div class="morning-stt">
                                <img src="${data.morningIcon}" alt="Morning Weather Icon" class="icon">
                                <p>강수 확률: 🌧${data.morningRain}%</p>
                                <span class="on">최저 기온: ${data.tempMorning}</span>
                            </div>
                        </div>
                        <div class="center">
                            <h3>${data.day}</h3>
                            <img src="${windDirectionData.image}" style="width: 80px; height: 100px; transform: rotate(${windDirectionData.rotation}deg);" alt="Wind Direction">
                            <br>
                            <span>💨풍속: ${data.WSD} m/s</span><br>
                            <span>💧습도: ${data.REH} %</span>
                        </div>  
                        <div class="afternoon-str">
                            <span class="cq">오후</span>
                            <div class="afternoon-stt">
                                <img src="${data.afternoonIcon}" alt="Afternoon Weather Icon" class="icon">
                                <p>강수 확률: 🌧${data.afternoonRain}%</p>
                                <span class="on2">최고 기온: ${data.tempAfternoon}</span>
                            </div>
                        </div>
                    </div>`;
                row1.appendChild(card);
                if (index === 0) row1.appendChild(document.createElement("br"));
            } else {
                row2.appendChild(card);
            }
        });

        // 로딩 스피너 숨기기
        document.getElementById("loader").style.display = "none";
    }

    // 페이지 로드 시 자동으로 날씨 데이터 로드
    await fetchWeatherData();

    // 날씨 데이터 가져오기
    async function fetchWeatherData() {
        const baseDate = document.getElementById("baseDate").value.trim();
        const baseTime = document.getElementById("baseTime").value.trim();
        const nx = parseInt(document.getElementById("nx").value.trim(), 10);
        const ny = parseInt(document.getElementById("ny").value.trim(), 10);
        const regId = document.getElementById("regId").value.trim();
        const tmFc = document.getElementById("tmFc").value.trim();
        const regIdTemp = document.getElementById("regIdTemp").value.trim();

        try {
            const response = await fetch('/residence/processAllWeather', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ baseDate, baseTime, nx, ny, regId, tmFc, regIdTemp }),
            });

            if (!response.ok) throw new Error(`Network response was not ok: ${response.status}`);

            const combinedData = await response.json();
            calculateMissingTMNAndTMX(combinedData);
            renderWeatherCards(combinedData);
        } catch (error) {
            console.error("데이터 가져오기 실패:", error);
            alert("데이터를 가져오는 중 문제가 발생했습니다. 다시 시도해주세요.");
        }
    }
});
