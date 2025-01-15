document.addEventListener("DOMContentLoaded", function() {
	// 지도 컨테이너
	var mapContainer = document.getElementById("map"); // 지도 표시할 div
	var mapOption = {
		level: 3, // 확대 레벨
	};

	var map;
	var markers = []; // 지도와 마커를 전역 변수로 선언
	var positions = [
		{ lat: 37.4979, lng: 127.0276, title: '강남구 - 호텔 AE' },  // 서울 강남구 테헤란로 120
		{ lat: 37.5307, lng: 127.1228, title: '강동구 - 리조트 AF' },  // 서울 강동구 강동대로 80
		{ lat: 37.6386, lng: 127.0263, title: '강북구 - 펜션 AG' },  // 서울 강북구 도봉로 110
		{ lat: 37.5503, lng: 126.8493, title: '강서구 - 모텔 AH' },  // 서울 강서구 공항대로 90
		{ lat: 37.4785, lng: 126.9517, title: '관악구 - 호텔 AI' },  // 서울 관악구 관악로 60
		{ lat: 37.5481, lng: 127.0771, title: '광진구 - 리조트 AJ' },  // 서울 광진구 광나루로 100
		{ lat: 37.4954, lng: 126.8828, title: '구로구 - 펜션 AK' },  // 서울 구로구 구로중앙로 80
		{ lat: 37.4523, lng: 126.9007, title: '금천구 - 모텔 AL' },  // 서울 금천구 금하로 70
		{ lat: 37.6547, lng: 127.0775, title: '노원구 - 호텔 AM' },  // 서울 노원구 노해로 120
		{ lat: 37.6540, lng: 127.0274, title: '도봉구 - 리조트 AN' },  // 서울 도봉구 도봉로 130
		{ lat: 37.5668, lng: 127.0415, title: '동대문구 - 펜션 AO' },  // 서울 동대문구 왕산로 110
		{ lat: 37.5114, lng: 126.9403, title: '동작구 - 모텔 AP' },  // 서울 동작구 노량진로 90
		{ lat: 37.5665, lng: 126.9028, title: '마포구 - 호텔 AQ' },  // 서울 마포구 월드컵로 150
		{ lat: 37.5771, lng: 126.9368, title: '서대문구 - 리조트 AR' },  // 서울 서대문구 연세로 100
		{ lat: 37.4845, lng: 127.0256, title: '서초구 - 펜션 AS' },  // 서울 서초구 반포대로 70
		{ lat: 37.5613, lng: 127.0394, title: '성동구 - 모텔 AT' },  // 서울 성동구 왕십리로 90
		{ lat: 37.5890, lng: 127.0178, title: '성북구 - 호텔 AU' },  // 서울 성북구 성북로 110
		{ lat: 37.5146, lng: 127.1238, title: '송파구 - 리조트 AV' },  // 서울 송파구 올림픽로 100
		{ lat: 37.5284, lng: 126.8717, title: '양천구 - 펜션 AW' },  // 서울 양천구 목동로 80
		{ lat: 37.5161, lng: 126.9012, title: '영등포구 - 모텔 AX' },  // 서울 영등포구 영등포로 90
		{ lat: 37.5326, lng: 126.9906, title: '용산구 - 호텔 AY' },  // 서울 용산구 한강대로 120
		{ lat: 37.6021, lng: 126.9203, title: '은평구 - 리조트 AZ' },  // 서울 은평구 은평로 100
		{ lat: 37.5720, lng: 126.9798, title: '종로구 - 펜션 BA' },  // 서울 종로구 종로 70
		{ lat: 37.5610, lng: 126.9973, title: '중구 - 모텔 BB' },  // 서울 중구 퇴계로 60
		{ lat: 37.6085, lng: 127.0856, title: '중랑구 - 호텔 BC' },   // 서울 중랑구 망우로 80

		{ lat: 35.1836, lng: 128.9517, title: '강서구 - 호텔 BD' },  // 부산 강서구 대저로 120
		{ lat: 35.2323, lng: 129.0705, title: '금정구 - 리조트 BE' },  // 부산 금정구 금정로 80
		{ lat: 35.2394, lng: 129.2292, title: '기장군 - 펜션 BF' },  // 부산 기장군 기장로 90
		{ lat: 35.1292, lng: 129.1147, title: '남구 - 모텔 BG' },  // 부산 남구 용호로 60
		{ lat: 35.0604, lng: 129.0365, title: '동구 - 호텔 BH' },  // 부산 동구 동대신로 110
		{ lat: 35.2325, lng: 129.0812, title: '동래구 - 리조트 BI' },  // 부산 동래구 명륜로 100
		{ lat: 35.1593, lng: 129.0583, title: '부산진구 - 펜션 BJ' },  // 부산 부산진구 서면로 70
		{ lat: 35.2704, lng: 129.0205, title: '북구 - 모텔 BK' },  // 부산 북구 덕천로 80
		{ lat: 35.1417, lng: 128.9827, title: '사상구 - 호텔 BL' },  // 부산 사상구 사상로 100
		{ lat: 35.1025, lng: 128.9818, title: '사하구 - 리조트 BM' },  // 부산 사하구 하단로 80
		{ lat: 35.0756, lng: 129.0031, title: '서구 - 펜션 BN' },  // 부산 서구 서대신로 90
		{ lat: 35.1651, lng: 129.1318, title: '수영구 - 모텔 BO' },  // 부산 수영구 광안로 70
		{ lat: 35.1796, lng: 129.0755, title: '연제구 - 호텔 BP' },  // 부산 연제구 연제로 100
		{ lat: 35.0898, lng: 129.0450, title: '영도구 - 리조트 BQ' },  // 부산 영도구 영도해변로 80
		{ lat: 35.1003, lng: 129.0403, title: '중구 - 펜션 BR' },   // 부산 중구 중구로 50

		{ lat: 37.5387, lng: 126.7315, title: '계양구 - 호텔 BS' },  // 인천 계양구 계양로 80
		{ lat: 37.4511, lng: 126.7045, title: '미추홀구 - 리조트 BT' },  // 인천 미추홀구 미추홀로 90
		{ lat: 37.4933, lng: 126.7312, title: '부평구 - 펜션 BU' },  // 인천 부평구 부평로 100
		{ lat: 37.4636, lng: 126.6280, title: '서구 - 모텔 BV' },  // 인천 서구 서곶로 70
		{ lat: 37.3841, lng: 126.6508, title: '연수구 - 호텔 BW' },  // 인천 연수구 송도로 120
		{ lat: 37.5084, lng: 126.5180, title: '옹진군 - 리조트 BX' },  // 인천 옹진군 옹진로 100
		{ lat: 37.4631, lng: 126.6164, title: '중구 - 펜션 BY' },  // 인천 중구 운서로 80
		{ lat: 37.4567, lng: 126.6295, title: '동구 - 모텔 BZ' },  // 인천 동구 송림로 60
		{ lat: 37.4480, lng: 126.7270, title: '남동구 - 호텔 CA' },  // 인천 남동구 남동대로 70
		{ lat: 37.7887, lng: 126.4802, title: '강화군 - 리조트 CB' },  // 인천 강화군 강화로 80

		{ lat: 36.3504, lng: 127.3845, title: '대덕구 - 호텔 CC' },  // 대전 대덕구 대덕대로 80
		{ lat: 36.3202, lng: 127.4337, title: '동구 - 리조트 CD' },  // 대전 동구 동대전로 100
		{ lat: 36.3351, lng: 127.3910, title: '서구 - 펜션 CE' },  // 대전 서구 둔산로 120
		{ lat: 36.3543, lng: 127.3658, title: '유성구 - 모텔 CF' },  // 대전 유성구 온천로 90
		{ lat: 36.3205, lng: 127.3842, title: '중구 - 호텔 CG' },   // 대전 중구 중앙로 110

		{ lat: 35.8555, lng: 128.6020, title: '남구 - 호텔 CH' },  // 대구 남구 대명로 100
		{ lat: 35.8432, lng: 128.5570, title: '달서구 - 리조트 CI' },  // 대구 달서구 달구벌대로 120
		{ lat: 35.8354, lng: 128.4846, title: '달성군 - 펜션 CJ' },  // 대구 달성군 논공로 90
		{ lat: 35.8761, lng: 128.6120, title: '동구 - 모텔 CK' },  // 대구 동구 동대구로 80
		{ lat: 35.9003, lng: 128.5547, title: '북구 - 호텔 CL' },  // 대구 북구 구암로 100
		{ lat: 35.8707, lng: 128.6193, title: '수성구 - 리조트 CM' },  // 대구 수성구 수성로 110
		{ lat: 35.8668, lng: 128.5910, title: '중구 - 펜션 CN' },  // 대구 중구 동성로 70
		{ lat: 35.8705, lng: 128.5611, title: '서구 - 모텔 CO' },   // 대구 서구 서대구로 90
		{ lat: 35.5374, lng: 129.3149, title: '남구 - 호텔 CP' },  // 울산 남구 성남로 90
		{ lat: 35.5022, lng: 129.4201, title: '동구 - 리조트 CQ' },  // 울산 동구 동대영로 110
		{ lat: 35.5686, lng: 129.3413, title: '북구 - 펜션 CR' },  // 울산 북구 천곡로 70
		{ lat: 35.5390, lng: 129.3119, title: '중구 - 모텔 CS' },  // 울산 중구 통북로 80
		{ lat: 35.5358, lng: 129.1889, title: '울주군 - 호텔 CT' },   // 울산 울주군 상북로 100
		{ lat: 37.8334, lng: 127.5218, title: '가평군 - 호텔 CU' },  // 경기도 가평군 가평로 90
		{ lat: 37.6487, lng: 126.8300, title: '고양시 - 리조트 CV' },  // 경기도 고양시 고양로 110
		{ lat: 37.4437, lng: 126.9983, title: '과천시 - 펜션 CW' },  // 경기도 과천시 중앙로 100
		{ lat: 37.4783, lng: 126.8666, title: '광명시 - 모텔 CX' },  // 경기도 광명시 광명로 80
		{ lat: 37.4173, lng: 127.2605, title: '광주시 - 호텔 CY' },  // 경기도 광주시 경충대로 120
		{ lat: 37.6019, lng: 127.1304, title: '구리시 - 리조트 CZ' },  // 경기도 구리시 구리로 90
		{ lat: 37.3582, lng: 126.9222, title: '군포시 - 펜션 DA' },  // 경기도 군포시 군포로 80
		{ lat: 37.6311, lng: 126.7375, title: '김포시 - 모텔 DB' },  // 경기도 김포시 김포로 100
		{ lat: 37.9014, lng: 127.0620, title: '동두천시 - 호텔 DC' },  // 경기도 동두천시 동두천로 110
		{ lat: 37.4833, lng: 126.7664, title: '부천시 - 리조트 DD' },  // 경기도 부천시 부천로 100
		{ lat: 37.4400, lng: 127.1276, title: '성남시 - 펜션 DE' },  // 경기도 성남시 성남로 80
		{ lat: 37.2634, lng: 127.0286, title: '수원시 - 모텔 DF' },  // 경기도 수원시 수원로 120
		{ lat: 37.3891, lng: 126.7852, title: '시흥시 - 호텔 DG' },  // 경기도 시흥시 시흥로 90
		{ lat: 37.3213, lng: 126.9300, title: '안산시 - 리조트 DH' },  // 경기도 안산시 안산로 100
		{ lat: 37.3833, lng: 126.9380, title: '안양시 - 펜션 DI' },  // 경기도 안양시 안양로 110
		{ lat: 37.1541, lng: 127.0692, title: '오산시 - 모텔 DJ' },  // 경기도 오산시 오산로 70
		{ lat: 37.2411, lng: 127.1796, title: '용인시 - 호텔 DK' },  // 경기도 용인시 용인로 80
		{ lat: 37.3596, lng: 126.9805, title: '의왕시 - 리조트 DL' },  // 경기도 의왕시 의왕로 90
		{ lat: 37.2705, lng: 127.4340, title: '이천시 - 펜션 DM' },  // 경기도 이천시 이천로 100
		{ lat: 37.7589, lng: 126.7732, title: '파주시에 - 모텔 DN' },  // 경기도 파주시에 파주로 80
		{ lat: 37.0505, lng: 127.1145, title: '평택시 - 호텔 DO' },  // 경기도 평택시 평택로 120
		{ lat: 37.8820, lng: 127.2089, title: '포천시 - 리조트 DP' },  // 경기도 포천시 포천로 100
		{ lat: 37.5433, lng: 127.2125, title: '하남시 - 펜션 DQ' },  // 경기도 하남시 하남로 70
		{ lat: 37.1841, lng: 127.0706, title: '화성시 - 모텔 DR' },  // 경기도 화성시 화성로 110
		{ lat: 37.8458, lng: 127.5280, title: '가평군 - 호텔 DS' },   // 경기도 가평군 가평로 80
		{ lat: 37.7664, lng: 128.8769, title: '강릉시 - 호텔 DT' },  // 강원 강릉시 강릉로 110
		{ lat: 38.4151, lng: 128.4401, title: '고성군 - 리조트 DU' },  // 강원 고성군 고성로 120
		{ lat: 38.2502, lng: 129.1247, title: '동해시 - 펜션 DV' },  // 강원 동해시 동해로 80
		{ lat: 38.4385, lng: 129.1600, title: '삼척시 - 모텔 DW' },  // 강원 삼척시 삼척로 90
		{ lat: 38.2010, lng: 128.5912, title: '속초시 - 호텔 DX' },  // 강원 속초시 속초로 100
		{ lat: 37.3382, lng: 127.9407, title: '원주시 - 리조트 DY' },  // 강원 원주시 원주로 110
		{ lat: 38.1281, lng: 128.2589, title: '인제군 - 펜션 DZ' },  // 강원 인제군 인제로 80
		{ lat: 37.4516, lng: 128.6673, title: '정선군 - 모텔 EA' },  // 강원 정선군 정선로 90
		{ lat: 38.2262, lng: 127.7362, title: '철원군 - 호텔 EB' },  // 강원 철원군 철원로 100
		{ lat: 37.8774, lng: 127.7340, title: '춘천시 - 리조트 EC' },  // 강원 춘천시 춘천로 110
		{ lat: 37.5445, lng: 129.1987, title: '태백시 - 펜션 ED' },  // 강원 태백시 태백로 80
		{ lat: 37.3897, lng: 128.6947, title: '평창군 - 모텔 EE' },  // 강원 평창군 평창로 90
		{ lat: 37.6740, lng: 127.7200, title: '홍천군 - 호텔 EF' },  // 강원 홍천군 홍천로 100
		{ lat: 38.1089, lng: 128.2567, title: '화천군 - 리조트 EG' },  // 강원 화천군 화천로 110
		{ lat: 38.4151, lng: 128.4401, title: '고성군 - 펜션 EH' },  // 강원 고성군 고성로 120
		{ lat: 37.3897, lng: 128.6947, title: '평창군 - 모텔 EI' },  // 강원 평창군 평창로 100
		{ lat: 37.1895, lng: 128.2210, title: '영월군 - 호텔 EJ' },  // 강원 영월군 영월로 110
		{ lat: 37.4516, lng: 128.6673, title: '정선군 - 리조트 EK' },   // 강원 정선군 정선로 120
		{ lat: 36.6249, lng: 127.6932, title: '괴산군 - 호텔 EL' },  // 충북 괴산군 괴산로 100
		{ lat: 36.9964, lng: 128.3775, title: '단양군 - 리조트 EM' },  // 충북 단양군 단양로 110
		{ lat: 36.5295, lng: 127.7107, title: '보은군 - 펜션 EN' },  // 충북 보은군 보은로 80
		{ lat: 36.2251, lng: 127.7443, title: '영동군 - 모텔 EO' },  // 충북 영동군 영동로 90
		{ lat: 36.9313, lng: 127.6970, title: '음성군 - 호텔 EP' },  // 충북 음성군 음성로 100
		{ lat: 37.1483, lng: 128.2057, title: '제천시 - 리조트 EQ' },  // 충북 제천시 제천로 110
		{ lat: 36.8889, lng: 127.4222, title: '진천군 - 펜션 ER' },  // 충북 진천군 진천로 90
		{ lat: 36.6274, lng: 127.4910, title: '청주시 - 모텔 ES' },  // 충북 청주시 청주로 100
		{ lat: 36.9887, lng: 127.9231, title: '충주군 - 호텔 ET' },  // 충북 충주시 충주로 110
		{ lat: 36.7761, lng: 126.4762, title: '태안군 - 리조트 EU' },  // 충북 태안군 태안로 120
		{ lat: 36.7663, lng: 126.6742, title: '홍성군 - 펜션 EV' },   // 충북 홍성군 홍성로 80
		{ lat: 36.3141, lng: 127.2886, title: '계룡시 - 호텔 EW' },  // 충남 계룡시 계룡로 100
		{ lat: 36.3402, lng: 127.2409, title: '공주시 - 리조트 EX' },  // 충남 공주시 공주로 110
		{ lat: 36.1258, lng: 127.4332, title: '금산군 - 펜션 EY' },  // 충남 금산군 금산로 80
		{ lat: 36.2040, lng: 127.0701, title: '논산시 - 모텔 EZ' },  // 충남 논산시 논산로 90
		{ lat: 36.7664, lng: 126.6815, title: '당진시 - 호텔 FA' },  // 충남 당진시 당진로 100
		{ lat: 36.7769, lng: 126.8000, title: '서산시 - 리조트 FB' },  // 충남 서산시 서산로 110
		{ lat: 36.0892, lng: 126.6915, title: '서천군 - 펜션 FC' },  // 충남 서천군 서천로 80
		{ lat: 36.7800, lng: 127.0305, title: '아산시 - 모텔 FD' },  // 충남 아산시 아산로 90
		{ lat: 36.4782, lng: 127.3640, title: '연기군 - 호텔 FE' },  // 충남 연기군 연기로 100
		{ lat: 36.6889, lng: 126.4787, title: '예산군 - 리조트 FF' },  // 충남 예산군 예산로 110
		{ lat: 36.8937, lng: 127.1322, title: '인주군 - 펜션 FG' },  // 충남 인주군 인주로 80
		{ lat: 36.8220, lng: 127.1516, title: '천안시 - 모텔 FH' },  // 충남 천안시 천안로 90
		{ lat: 36.5967, lng: 126.6987, title: '홍성군 - 호텔 FI' },  // 충남 홍성군 홍성로 100
		{ lat: 36.6609, lng: 126.9689, title: '화성시 - 리조트 FJ' },  // 충남 화성시 화성로 110
		{ lat: 36.3141, lng: 127.2886, title: '계룡시 - 호텔 FG' },  // 충남 계룡시 계룡로 100
		{ lat: 36.1258, lng: 127.4332, title: '금산군 - 리조트 FH' },  // 충남 금산군 금산로 110
		{ lat: 36.2040, lng: 127.0701, title: '논산시 - 펜션 FI' },  // 충남 논산시 논산로 80
		{ lat: 36.7664, lng: 126.6815, title: '당진시 - 모텔 FJ' },  // 충남 당진시 당진로 90
		{ lat: 36.7769, lng: 126.8000, title: '서산시 - 호텔 FK' },  // 충남 서산시 서산로 100
		{ lat: 36.0892, lng: 126.6915, title: '서천군 - 리조트 FL' },  // 충남 서천군 서천로 110
		{ lat: 36.7800, lng: 127.0305, title: '아산시 - 펜션 FM' },  // 충남 아산시 아산로 80
		{ lat: 36.4782, lng: 127.3640, title: '연기군 - 모텔 FN' },  // 충남 연기군 연기로 90
		{ lat: 36.6889, lng: 126.4787, title: '예산군 - 호텔 FO' },  // 충남 예산군 예산로 100
		{ lat: 36.8937, lng: 127.1322, title: '인주군 - 리조트 FP' },  // 충남 인주군 인주로 110
		{ lat: 36.8220, lng: 127.1516, title: '천안시 - 펜션 FQ' },  // 충남 천안시 천안로 80
		{ lat: 36.5967, lng: 126.6987, title: '홍성군 - 모텔 FR' },   // 충남 홍성군 홍성로 90
		{ lat: 35.8256, lng: 127.1480, title: '전주시 - 호텔 FX' },  // 전북 전주시 전주로 100
		{ lat: 35.9919, lng: 126.7340, title: '군산시 - 리조트 FY' },  // 전북 군산시 군산로 110
		{ lat: 35.9485, lng: 126.9645, title: '익산시 - 펜션 FZ' },  // 전북 익산시 익산로 90
		{ lat: 35.5558, lng: 126.8831, title: '정읍시 - 모텔 GA' },  // 전북 정읍시 정읍로 100
		{ lat: 35.3755, lng: 127.3655, title: '남원시 - 호텔 GB' },  // 전북 남원시 남원로 110
		{ lat: 35.7980, lng: 126.8830, title: '김제시 - 리조트 GC' },  // 전북 김제시 김제로 120
		{ lat: 35.3391, lng: 127.2271, title: '완주군 - 펜션 GD' },  // 전북 완주군 완주로 90
		{ lat: 35.4257, lng: 126.7107, title: '고창군 - 모텔 GE' },  // 전북 고창군 고창로 100
		{ lat: 35.5803, lng: 126.7779, title: '부안군 - 호텔 GF' },  // 전북 부안군 부안로 110
		{ lat: 35.5731, lng: 127.4980, title: '임실군 - 리조트 GG' },  // 전북 임실군 임실로 120
		{ lat: 35.4001, lng: 127.3614, title: '순창군 - 펜션 GH' },  // 전북 순창군 순창로 80
		{ lat: 35.6954, lng: 127.3742, title: '진안군 - 모텔 GI' },  // 전북 진안군 진안로 90
		{ lat: 35.7232, lng: 127.7110, title: '무주군 - 호텔 GJ' },  // 전북 무주군 무주로 110
		{ lat: 35.6127, lng: 127.7209, title: '장수군 - 리조트 GK' },   // 전북 장수군 장수로 120
		{ lat: 34.8100, lng: 126.3926, title: '목포시 - 호텔 GL' },  // 전남 목포시 목포로 100
		{ lat: 34.7605, lng: 127.6625, title: '여수시 - 리조트 GM' },  // 전남 여수시 여수로 120
		{ lat: 34.9467, lng: 127.5106, title: '순천시 - 펜션 GN' },  // 전남 순천시 순천로 90
		{ lat: 34.9506, lng: 127.7009, title: '광양시 - 모텔 GO' },  // 전남 광양시 광양로 80
		{ lat: 35.0176, lng: 126.7343, title: '나주시 - 호텔 GP' },  // 전남 나주시 나주로 100
		{ lat: 34.4792, lng: 126.2367, title: '진도군 - 리조트 GQ' },  // 전남 진도군 진도로 110
		{ lat: 34.9921, lng: 126.3806, title: '무안군 - 펜션 GR' },  // 전남 무안군 무안로 90
		{ lat: 34.5749, lng: 126.5955, title: '해남군 - 모텔 GS' },  // 전남 해남군 해남로 80
		{ lat: 35.1705, lng: 126.4744, title: '영광군 - 호텔 GT' },  // 전남 영광군 영광로 100
		{ lat: 35.2558, lng: 126.9873, title: '장성군 - 리조트 GU' },  // 전남 장성군 장성로 120
		{ lat: 34.2923, lng: 126.7747, title: '완도군 - 펜션 GV' },  // 전남 완도군 완도로 80
		{ lat: 34.5810, lng: 127.3611, title: '고흥군 - 모텔 GW' },  // 전남 고흥군 고흥로 90
		{ lat: 34.7465, lng: 127.2619, title: '보성군 - 호텔 GX' },  // 전남 보성군 보성로 100
		{ lat: 35.0293, lng: 126.9925, title: '화순군 - 리조트 GY' },  // 전남 화순군 화순로 110
		{ lat: 35.0703, lng: 126.5637, title: '함평군 - 펜션 GZ' },  // 전남 함평군 함평로 80
		{ lat: 34.8069, lng: 126.7229, title: '영암군 - 모텔 HA' },  // 전남 영암군 영암로 90
		{ lat: 34.6100, lng: 126.7866, title: '강진군 - 호텔 HB' },  // 전남 강진군 강진로 100
		{ lat: 34.6266, lng: 126.8785, title: '장흥군 - 리조트 HC' },  // 전남 장흥군 장흥로 120
		{ lat: 35.3074, lng: 126.9783, title: '담양군 - 펜션 HD' },  // 전남 담양군 담양로 90
		{ lat: 34.8679, lng: 126.0003, title: '신안군 - 모텔 HE' },  // 전남 신안군 신안로 80
		{ lat: 34.2923, lng: 126.7747, title: '완도군 - 호텔 HF' },   // 전남 완도군 완도로 100
		{ lat: 36.0330, lng: 129.3436, title: '포항시 - 호텔 HG' },  // 경북 포항시 포항로 100
		{ lat: 35.8566, lng: 129.2247, title: '경주시 - 리조트 HH' },  // 경북 경주시 경주로 120
		{ lat: 36.1375, lng: 128.1122, title: '김천시 - 펜션 HI' },  // 경북 김천시 김천로 90
		{ lat: 36.5663, lng: 128.7299, title: '안동시 - 모텔 HJ' },  // 경북 안동시 안동로 80
		{ lat: 36.1160, lng: 128.3471, title: '구미시 - 호텔 HK' },  // 경북 구미시 구미로 100
		{ lat: 36.5597, lng: 129.1561, title: '영주시 - 리조트 HL' },  // 경북 영주시 영주로 110
		{ lat: 35.9806, lng: 128.9375, title: '영천시 - 펜션 HM' },  // 경북 영천시 영천로 90
		{ lat: 36.4545, lng: 128.1545, title: '상주시 - 모텔 HN' },  // 경북 상주시 상주로 80
		{ lat: 36.5748, lng: 128.1903, title: '문경시 - 호텔 HO' },  // 경북 문경시 문경로 100
		{ lat: 35.8460, lng: 128.7365, title: '경산시 - 리조트 HP' },  // 경북 경산시 경산로 120
		{ lat: 36.0403, lng: 128.6336, title: '군위군 - 펜션 HQ' },  // 경북 군위군 군위로 90
		{ lat: 36.2249, lng: 128.5874, title: '의성군 - 모텔 HR' },  // 경북 의성군 의성로 80
		{ lat: 35.5490, lng: 128.6984, title: '청도군 - 호텔 HS' },  // 경북 청도군 청도로 100
		{ lat: 35.7410, lng: 128.5203, title: '고령군 - 리조트 HT' },  // 경북 고령군 고령로 110
		{ lat: 35.9734, lng: 128.3682, title: '성주군 - 펜션 HU' },  // 경북 성주군 성주로 90
		{ lat: 35.9279, lng: 128.4141, title: '칠곡군 - 모텔 HV' },  // 경북 칠곡군 칠곡로 80
		{ lat: 36.6610, lng: 128.2919, title: '예천군 - 호텔 HW' },  // 경북 예천군 예천로 100
		{ lat: 36.8987, lng: 129.1497, title: '봉화군 - 리조트 HX' },  // 경북 봉화군 봉화로 120
		{ lat: 36.9361, lng: 129.4236, title: '울진군 - 펜션 HY' },  // 경북 울진군 울진로 90
		{ lat: 37.4817, lng: 131.9430, title: '울릉군 - 모텔 HZ' },  // 경북 울릉군 울릉로 80
		{ lat: 36.4655, lng: 129.4300, title: '영덕군 - 호텔 IA' },  // 경북 영덕군 영덕로 100
		{ lat: 36.4054, lng: 129.0343, title: '청송군 - 리조트 IB' },   // 경북 청송군 청송로 110
		{ lat: 35.2288, lng: 128.8803, title: '김해시 - 리조트 IC' },  // 경남 김해시 김해로 120
		{ lat: 35.1897, lng: 128.1048, title: '진주시 - 펜션 ID' },  // 경남 진주시 진주로 90
		{ lat: 35.3370, lng: 129.0387, title: '양산시 - 모텔 IE' },  // 경남 양산시 양산로 80
		{ lat: 34.9014, lng: 128.6191, title: '거제시 - 호텔 IF' },  // 경남 거제시 거제로 100
		{ lat: 34.8492, lng: 128.4335, title: '통영시 - 리조트 IG' },  // 경남 통영시 통영로 110
		{ lat: 35.2390, lng: 128.7426, title: '밀양시 - 펜션 IH' },  // 경남 밀양시 밀양로 90
		{ lat: 35.2470, lng: 128.4242, title: '함안군 - 모텔 II' },  // 경남 함안군 함안로 80
		{ lat: 35.0770, lng: 128.0884, title: '사천시 - 호텔 IJ' },  // 경남 사천시 사천로 100
		{ lat: 35.2039, lng: 127.7667, title: '하동군 - 리조트 IK' },  // 경남 하동군 하동로 120
		{ lat: 35.2324, lng: 128.6002, title: '창녕군 - 펜션 IL' },  // 경남 창녕군 창녕로 90
		{ lat: 34.9597, lng: 128.3282, title: '고성군 - 모텔 IM' },  // 경남 고성군 고성로 80
		{ lat: 34.8313, lng: 128.7429, title: '남해군 - 호텔 IN' },  // 경남 남해군 남해로 100
		{ lat: 35.0749, lng: 128.5944, title: '산청군 - 리조트 IO' },  // 경남 산청군 산청로 110
		{ lat: 35.4181, lng: 128.0354, title: '합천군 - 펜션 IP' },  // 경남 합천군 합천로 90
		{ lat: 35.2745, lng: 128.0716, title: '의령군 - 모텔 IQ' },  // 경남 의령군 의령로 80
		{ lat: 35.1697, lng: 128.6899, title: '진해시 - 호텔 IR' },  // 경남 진해시 진해로 100
		{ lat: 35.2285, lng: 128.6810, title: '창원시 - 리조트 IS' },  // 경남 창원시 창원로 130
		{ lat: 35.2320, lng: 128.8805, title: '김해시 - 펜션 IT' },   // 경남 김해시 김해로 130
		{ lat: 33.4996, lng: 126.5310, title: '제주시 - 호텔 IU' },  // 제주 제주시 제주로 100
		{ lat: 33.4997, lng: 126.5321, title: '제주시 - 리조트 IV' }   // 제주 제주시 제주로 120
	];

	// 현재 위치로 지도 중심을 설정하기 전에, 위치를 가져와야 함
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position) {
			// 사용자의 현재 위치
			var currentLat = position.coords.latitude; // 위도
			var currentLon = position.coords.longitude; // 경도

			// 지도 옵션에 현재 위치 설정
			mapOption.center = new kakao.maps.LatLng(currentLat, currentLon);

			// 카카오 지도 생성
			map = new kakao.maps.Map(mapContainer, mapOption); // 지도 객체 생성

			// 지도 중심에 마커 설정
			var marker = new kakao.maps.Marker({
				position: map.getCenter(), // 현재 위치에 마커 표시
				map: map,
			});

			// 지도 중심 변경
			map.setCenter(new kakao.maps.LatLng(currentLat, currentLon));

			// 기존 마커들 추가
			addMarkers(); // positions 배열에 있는 마커들을 추가

		}, function(error) {
			alert("현재 위치 정보를 가져올 수 없습니다.");
		});
	} else {
		alert("현재 위치를 지원하지 않는 브라우저입니다.");
	}

	// 여러 마커 추가하는 함수
	function addMarkers() {
		// positions 배열에 있는 각 좌표에 대해 마커를 생성하여 지도에 추가
		for (var i = 0; i < positions.length; i++) {
			var position = new kakao.maps.LatLng(positions[i].lat, positions[i].lng);
			var marker = new kakao.maps.Marker({
				position: position,
				title: positions[i].title,
				map: map
			});
			markers.push(marker); // 마커 배열에 추가
		}
	}

	// 주소 검색 함수
	function searchAddress() {
		new daum.Postcode({
			oncomplete: function(data) {
				var fullAddress = data.address; // 전체 주소
				document.getElementById("address").value = fullAddress;
				document.getElementById("address-hidden").value = fullAddress;

				// Geocoder를 통해 좌표 변환
				var geocoder = new kakao.maps.services.Geocoder();
				geocoder.addressSearch(fullAddress, function(results, status) {
					if (status === kakao.maps.services.Status.OK) {
						var result = results[0];
						var lat = parseFloat(result.y); // 위도
						var lng = parseFloat(result.x); // 경도

						// 좌표 값 유효성 검사
						if (!isNaN(lat) && !isNaN(lng)) {
							document.getElementById("latitudeNum").value = lat;
							document.getElementById("longitudeNum").value = lng;

							// 기상청 좌표 변환
							var gridCoordinates = dfs_xy_conv("toXY", lat, lng);
							document.getElementById("nx").value = gridCoordinates.x;
							document.getElementById("ny").value = gridCoordinates.y;

							// 지도 중심 이동
							updateMap(lat, lng);
						} else {
							alert("좌표를 가져오지 못했습니다.");
						}
					} else {
						alert("주소 검색에 실패했습니다.");
					}
				});
			}
		}).open();
	}

	// 지도 및 마커 업데이트
	function updateMap(latitude, longitude) {
		var position = new kakao.maps.LatLng(latitude, longitude); // 새로운 좌표 생성
		map.setCenter(position); // 지도 중심 변경
	}

	// 버튼 클릭 시 주소 검색
	document.getElementById("addressBtn").addEventListener("click", function() {
		searchAddress();
	});
});

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
	}
	return rs;
}