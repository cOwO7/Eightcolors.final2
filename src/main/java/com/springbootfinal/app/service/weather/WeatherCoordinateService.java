package com.springbootfinal.app.service.weather;

import com.springbootfinal.app.domain.weather.WeatherCoordinate;
import com.springbootfinal.app.mapper.weather.WeatherCoordinateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherCoordinateService {

	@Autowired
    private WeatherCoordinateMapper weatherCoordinateMapper;

	public WeatherCoordinate getCoordinatesByKorCode(String korCode) {
	    return weatherCoordinateMapper.getCoordinatesByKorCode(korCode);
	}
}
