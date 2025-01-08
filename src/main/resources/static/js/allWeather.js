document.addEventListener("DOMContentLoaded", () => {
	const weatherForm = document.querySelector("form");
	const combinedWeatherTableBody = document.getElementById("combinedWeatherTableBody");
	const weatherContainer = document.getElementById("weatherContainer");

	// 폼 제출 이벤트 처리
	weatherForm.addEventListener("submit", async (event) => {
		event.preventDefault(); // 폼 제출로 인한 페이지 리로드 방지

		// 입력 필드에서 값 가져오기
		const baseDate = document.getElementById("baseDate").value.trim();
		const baseTime = document.getElementById("baseTime").value.trim();
		const nx = parseInt(document.getElementById("nx").value.trim(), 10);
		const ny = parseInt(document.getElementById("ny").value.trim(), 10);
		const regId = document.getElementById("regId").value.trim();
		const tmFc = document.getElementById("tmFc").value.trim();
		const regIdTemp = document.getElementById("regIdTemp").value.trim();

		try {
			// API 요청 보내기
			const response = await fetch('/processAllWeather', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
				},
				body: JSON.stringify({
					baseDate,
					baseTime,
					nx,
					ny,
					regId,
					tmFc,
					regIdTemp,
				}),
			});

			if (!response.ok) {
				throw new Error(`Network response was not ok: ${response.status}`);
			}

			const combinedData = await response.json();
			console.log("Fetched Data:", combinedData);

			addCombinedDataToTable(combinedData);
			renderWeatherCards(combinedData);
		} catch (error) {
			console.error("데이터 가져오기 실패:", error);
			alert("데이터를 가져오는 중 문제가 발생했습니다. 다시 시도해주세요.");
		}
	});

	// 테이블에 데이터 추가 함수
	const addCombinedDataToTable = (combinedData) => {
		if (!combinedWeatherTableBody) {
			console.error("combinedWeatherTableBody 요소가 존재하지 않습니다.");
			return;
		}

		combinedWeatherTableBody.innerHTML = ''; // 기존 데이터 삭제

		// 오늘 날짜를 기준으로 데이터 정렬
		const sortedData = Object.entries(combinedData).sort(([dateA], [dateB]) => dateA.localeCompare(dateB));

		// 테이블에 데이터 추가
		sortedData.forEach(([dateKey, details]) => {
			const row = document.createElement("tr");

			if (details.SKY || details.POP) {
				// 단기 예보 데이터
				row.innerHTML = `
                <td>${dateKey}</td>
                <td>단기 예보</td>
                <td>${details.SKY || '--'}</td>
                <td>${details.POP || '--'}</td>
                <td>${details.TMN || details.TMP || details.minTemperature || '--'}</td>
                <td>${details.TMX || details.maxTemperature || '--'}</td>
                `;
			} else if (details.minTemperature || details.maxTemperature) {
				// 중기 예보 데이터
				row.innerHTML = `
                <td>${dateKey}</td>
                <td>중기 예보</td>
                <td>${details.weatherForecast || '--'}</td>
                <td>${details.rainProbability || '--'}</td>
                <td>${details.minTemperature || '--'}</td>
                <td>${details.maxTemperature || '--'}</td>
                `;
			}

			combinedWeatherTableBody.appendChild(row);
		});
	};

	// 날씨 데이터를 카드로 렌더링
	const renderWeatherCards = (combinedData) => {
		if (!weatherContainer) {
			console.error("weatherContainer 요소가 존재하지 않습니다.");
			return;
		}

		weatherContainer.innerHTML = ''; // 기존 카드 삭제

		const today = new Date();
		const options = { month: "numeric", day: "numeric" }; // Format: MM.DD

		const weatherData = Object.entries(combinedData).map(([dateKey, details], index) => {
			const futureDate = new Date(today);
			futureDate.setDate(today.getDate() + index);

			return {
				day: index === 0 ? "오늘" : index === 1 ? "내일" : `D+${index}`,
				date: futureDate.toLocaleDateString("ko-KR", options),
				icon: `${details.SKY || 'sunny'}.img`, // 동적 SKY 값 기반
				tempMorning: `${details.TMN || details.TMP || details.minTemperature || '--'}°`,
				tempAfternoon: `${details.TMX || details.maxTemperature || '--'}°`,
				rainMorning: `${details.POP || details.rainProbability || '--'}%`,
				rainAfternoon: `${details.POP || details.rainProbability || '--'}%`
			};
		});

		// 카드 생성
		weatherData.forEach((data, index) => {
			const card = document.createElement("div");
			card.className = "day-card";
			if (index === 0) card.classList.add("today"); // 오늘 강조

			card.innerHTML = `
                <h3>${data.day}</h3>
                <p>${data.date}</p>
                <img src="${data.icon}" alt="Weather Icon" class="icon">
                <p class="temp">${data.tempMorning} / ${data.tempAfternoon}</p>
                <p class="rain">🌧 ${data.rainMorning} / ${data.rainAfternoon}</p>
            `;
			weatherContainer.appendChild(card);
		});
	};
});
