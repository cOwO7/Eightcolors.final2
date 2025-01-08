package com.springbootfinal.app.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WeatherForecast {
    private String date; // 날짜
    private String time; // 시간
    private String temperature; // 기온
    private String humidity; // 습도
    private String precipitation; // 강수량
    private String windSpeed; // 풍속
}
