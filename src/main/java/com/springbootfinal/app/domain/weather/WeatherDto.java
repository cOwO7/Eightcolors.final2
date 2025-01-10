package com.springbootfinal.app.domain.weather;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WeatherDto {

	Integer nx;
	Integer ny;
    String baseDate;
    String baseTime;
    String regld;
    String tmFc;

    public WeatherDto(String baseDate, String baseTime, Integer nx, Integer ny) {
        this.nx = nx;
        this.ny = ny;
        this.baseDate = baseDate;
        this.baseTime = baseTime;
    }
}
