package com.springbootfinal.app.domain.weather;

import lombok.Data;

//@Getter
//@Setter
@Data
public class AllWeatherDto {
    private String baseDate;
    private String baseTime;
    private Integer nx;
    private Integer ny;
    private String regId;
    private String tmFc;
    private String regIdTemp;
}
