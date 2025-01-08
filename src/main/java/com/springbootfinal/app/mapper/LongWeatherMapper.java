package com.springbootfinal.app.mapper;

import com.springbootfinal.app.domain.LongWeatherDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LongWeatherMapper {
	
	List<LongWeatherDto> getLongWeatherData(String regId, String tmFc);
}
