package com.springbootfinal.app.mapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.springbootfinal.app.domain.ResidenceSearch;

@Mapper
public interface AccommodationSearchMapper {
	
	List<ResidenceSearch> findAvailableResidences(
			 @Param("searchKeyword") String searchKeyword,
	        @Param("checkinDate") LocalDate checkinDate, 
	        @Param("checkoutDate") LocalDate checkoutDate,
	        @Param("startRow") int startRow, @Param("pageSize") int pageSize
	       
	    );
	   
	List<ResidenceSearch> findAllResidences(@Param("startRow") int startRow, @Param("pageSize") int pageSize);

	   
	   public int accommodationCount();
	   
	   public int findAvailableResidencesCount(@Param("searchKeyword") String searchKeyword,
		        @Param("checkinDate") LocalDate checkinDate, 
		        @Param("checkoutDate") LocalDate checkoutDate);

}
