package com.springbootfinal.app.mapper.weather;

import com.springbootfinal.app.domain.weather.WeatherCoordinate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WeatherCoordinateMapper {

	// 도시와 구/시 이름으로 좌표를 조회하는 메서드 (XML에서 매핑된 쿼리 호출)
    WeatherCoordinate getCoordinatesByAreaName(
    		@Param("areaName") String areaName);
	
	WeatherCoordinate findByKorCode(String korCode);

	List<WeatherCoordinate> findAll();

	WeatherCoordinate getCoordinatesByKorCode(String korCode);
}
