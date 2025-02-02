package com.springbootfinal.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.springbootfinal.app.domain.ResidenceSearch;

@Mapper
public interface AccommodationSearchMapper {
	
	   List<ResidenceSearch> findAvailableResidences(
		        @Param("checkinDate") String checkinDate, 
		        @Param("checkoutDate") String checkoutDate
		    );
}
