package com.springbootfinal.app.mapper.weather;

import com.springbootfinal.app.domain.weather.LongWeatherDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LongWeatherMapper {
	
	List<LongWeatherDto> getLongWeatherData(String regId, String tmFc);
}
