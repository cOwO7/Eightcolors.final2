package com.springbootfinal.app.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.springbootfinal.app.domain.ResidenceSearch;

@Mapper
public interface AccommodationSearchMapper {
	
	List<ResidenceSearch> findAvailableResidences(
			 @Param("searchKeyword") String searchKeyword,
	        @Param("checkinDate") LocalDate checkinDate, 
	        @Param("checkoutDate") LocalDate checkoutDate
	       
	    );
	   
	   List<ResidenceSearch> findAllResidences();
}
