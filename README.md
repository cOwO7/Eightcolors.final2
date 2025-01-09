# 2Team Final Project

## 프로그램 소개
+ **Title: 오늘어때**
+ **Introduction: "오늘어때"** 는 날씨 데이터를 기반으로 한 혁신적인 숙박 예약 플랫폼입니다. 이 프로젝트는 숙박업소의 공실률을 최소화하고, 소비자들에게 합리적인 비용으로 숙박과 여행을 즐길 기회를 제공하기 위해 기획되었습니다.
## 프로그램 주요 기능
1. **날씨 데이터 기반 할인 예약 시스템**
+ 기상 상황에 따라 예약 가격을 유연하게 조정합니다.
  + 예: 눈이나 비가 오는 날에는 특별 할인을 제공하여 숙박업소의 공실을 줄이고, 여행객들이 저렴한 비용으로 숙박을 예약할 수 있도록 돕습니다.
+ 숙박업소 입장에서는 공실 상태로 방을 두는 것보다 할인된 가격으로 손님을 맞이함으로써 수익을 극대화할 수 있습니다.

2. **여행 계획 지원**
+ 날씨 데이터를 기반으로 여행객들에게 맞춤형 여행 추천을 제공합니다.
  + 날씨가 맑은 날: 야외 활동 추천 (예: 산책로, 해변, 공원 등).
  + 비나 눈이 오는 날: 리뷰와 데이터를 분석하여 실내 데이트 코스와 즐길 거리(예: 카페, 박물관, 실내 체험 활동 등)를 브리핑합니다.
+ 여행자들에게 날씨에 구애받지 않고 완벽한 여행 경험을 누릴 수 있도록 도움을 줍니다.

3. **리뷰 및 사용자 피드백 기반 추천 시스템**
+ 사용자 리뷰를 활용해 특정 날씨 조건에 적합한 장소와 활동을 추천합니다.
  + 예: "눈 오는 날 방문하기 좋은 로맨틱한 카페" 또는 "비 오는 날 즐길 수 있는 실내 체험 장소".
+ 이러한 기능은 여행객이 날씨에 상관없이 만족스러운 경험을 누릴 수 있도록 도와줍니다.

### 사용자 입장
+ **날씨 기반 할인:**
  + 여행객은 일반적인 요금보다 저렴한 비용으로 숙박을 예약할 수 있습니다.
  + 기상 상황에 맞춘 할인은 특히 갑작스러운 여행 계획에도 경제적인 부담을 덜어줍니다.

+ **날씨와 연계된 여행 코스 추천:**
  + 여행자는 날씨에 맞는 코스를 쉽게 찾을 수 있습니다.
  + 예: 비 오는 날 가기 좋은 실내 전시회, 눈 오는 날 방문하기 좋은 힐링 카페 등.

+ **리뷰를 기반으로 한 믿을 수 있는 추천:**
  + 다른 이용자들의 리뷰를 바탕으로 신뢰성 높은 여행 정보를 제공받습니다.

### 숙박업소 입장
+ **공실률 최소화:**
  + 비수기, 기상 악화 등으로 공실이 늘어나는 상황을 최소화합니다.
  + 할인 가격으로라도 방이 예약되면, 공실로 인한 손해를 줄이고 수익성을 유지할 수 있습니다.

+ **수요를 예측하여 가격 최적화:**
  + 날씨 데이터와 예약 데이터를 분석해 최적의 가격 전략을 수립할 수 있습니다.

## 프로그램 라이브러리
### Java <img src="https://cdn-icons-png.flaticon.com/512/226/226777.png" width="auto" height="20" />
+ Java Version: 17

### Spring Boot <img src="https://blog.kakaocdn.net/dn/bpihJ7/btqGyJc68sl/xJYETAqF9pjMJCT9Adkv90/img.png" width="auto" height="20" />
+ Spring Boot Version: 3.4.0
+ Spring Boot Starter Dependencies
+ Spring Boot Logging: 3.4.0
+ Spring Boot Data JPA: 3.4.0
+ Spring Boot DevTools: 3.4.0
+ Spring Boot Security: 3.4.0
+ Spring Boot Thymeleaf: 3.4.0
+ Spring Boot Web: 3.4.0
+ Spring Boot WebFlux: 3.4.0
+ Spring Boot WebSocket: 3.4.0
+ Spring Boot JDBC: 3.4.0
+ Spring Boot Actuator: 3.4.0
+ Spring Boot OAuth2 Client: 3.4.0

### Spring Retry <img src="https://blog.kakaocdn.net/dn/bpihJ7/btqGyJc68sl/xJYETAqF9pjMJCT9Adkv90/img.png" width="auto" height="20" />
+ Version: 1.3.1

### Tomcat <img src="https://w7.pngwing.com/pngs/264/942/png-transparent-apache-tomcat-apache-http-server-vulnerability-computer-software-java-servlet-others-cat-like-mammal-carnivoran-text-thumbnail.png" width="auto" height="20" />
+ Apache Tomcat 10.1.33

### AspectJ <img src="https://darpan.blog/wp-content/uploads/2016/10/Aspectj.jpg" width="auto" height="20" />
+ Version: 1.9.9

### JSON and Jackson <img src="https://velog.velcdn.com/images/kmjstj3/post/e0bba8ca-e624-47a9-963e-93c6e8b3063e/image.png" width="auto" height="20" />
+ Jackson XML: 2.15.0 (from transitive dependencies)
+ Jackson Databind: 2.15.0 (from transitive dependencies)
+ JSON (org.json): 20240303

### MyBatis <img src="https://img1.daumcdn.net/thumb/R800x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2F1FEJg%2FbtqXN9E2U0S%2Ff9shXkE6wyvIKN9Xv5zfn1%2Fimg.jpg" width="auto" height="20" />
+ MyBatis Spring Boot Starter: 3.0.3
+ MyBatis Spring Boot Starter Test: 3.0.3

### Lombok <img src="https://img1.daumcdn.net/thumb/R750x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbsCjBi%2FbtsIHcJ3Ani%2FoD0I4gr3oxMOufx0ilKgC1%2Fimg.png" width="auto" height="20" />
+ Lombok: Latest (from Gradle dependency resolution)

### Timefold Solver <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSOc3IZ_b4PWg3KnxADcU4JrjBCoHtx0rkbKQ&s" width="auto" height="20" />
+ Version: 1.15.0

### Thymeleaf <img src="https://blog.kakaocdn.net/dn/cq2C2H/btsHtNY6KYD/MmxnjIKEyUq2Wx8L0GFT80/img.png" width="auto" height="30" />
+ Thymeleaf Layout Dialect: Latest (transitive resolution)
+ Thymeleaf Extras Spring Security 6: Latest (transitive resolution)

### MySQL <img src="https://cdn-icons-png.flaticon.com/512/919/919836.png" width="auto" height="20" />
+ MySQL Connector/J: Latest (runtime-only dependency)

### JAXB (Jakarta Binding API) <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQH2FIoobOUrkq3lUV7zpjzKHdoJqOnIPtrtA&s" width="auto" height="20" />
+ Jakarta XML Bind API: 3.0.0
+ JAXB Runtime: 3.0.0

### JUnit and Testing <img src="https://nesoy.github.io/assets/logo/JUnit.png" width="auto" height="20" />
+ JUnit Platform Launcher: Latest
+ Spring Boot Starter Test: 3.4.0
+ Spring Security Test: 3.4.0

### Servlet API <img src="https://cdn-icons-png.flaticon.com/512/919/919859.png" width="16" height="16" />
+ Servlet API: 4.0.1
+ Spring Security Web: 6.0.0
+ Spring Security Config: 6.0.0

### NAVER <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/2/23/Naver_Logotype.svg/1280px-Naver_Logotype.svg.png" width="auto" height="16">
+ 지도, 소셜로그인 API

### KAKAO <img src="data:image/png;base64,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" width="auto" height="16">
+ 지도, 소셜로그인 API

### 공공데이터 포털 <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRK6CNvu119-aEqSgBQJB_vT8nnsJZRhDRg0A&s" width="auto" height="16" />
+ 기상청, 일몰/일출 API

### iCON
+ Rian Maulana - Flaticon


