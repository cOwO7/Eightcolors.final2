package com.springbootfinal.app.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springbootfinal.app.domain.ResidenceSearch;
import com.springbootfinal.app.mapper.AccommodationSearchMapper;

@Service
public class AccommodationSearchService {
	
	@Autowired
	private AccommodationSearchMapper accommodationSearchMapper;
	
	private static final int PAGE_SIZE = 10;
	
	private static final int PAGE_GROUP = 10;
	
	
	
	public Map<String, Object> getAvailableResidences(
		    String searchKeyword, 
		    LocalDate checkinDate, 
		    LocalDate checkoutDate, 
		    int pageNum) {
		    
		    int currentPage = pageNum;
		    int startRow = (currentPage - 1) * PAGE_SIZE;  // 상수 PAGE_SIZE 사용
		    
		    int listCount = accommodationSearchMapper.findAvailableResidencesCount(searchKeyword, checkinDate, checkoutDate);
		    
		    List<ResidenceSearch> searchList = accommodationSearchMapper.findAvailableResidences(searchKeyword, checkinDate, checkoutDate, currentPage, startRow);
		    
		    int pageCount = 
		        listCount / PAGE_SIZE + (listCount % PAGE_SIZE == 0 ? 0 : 1); // 상수 PAGE_SIZE 사용
		    
		    int startPage = (currentPage / PAGE_GROUP) * PAGE_GROUP + 1
		            - (currentPage % PAGE_GROUP == 0 ? PAGE_GROUP : 0);
		    
		    int endPage = startPage + PAGE_GROUP - 1;
		    
		    if (endPage > pageCount) {
		        endPage = pageCount;
		    }
		    
		    Map<String, Object> modelMap = new HashMap<>();
		    modelMap.put("results", searchList);
		    modelMap.put("pageCount", pageCount);
		    modelMap.put("startPage", startPage);
		    modelMap.put("endPage", endPage);
		    modelMap.put("currentPage", currentPage);
		    modelMap.put("listCount", listCount);
		    modelMap.put("pageGroup", PAGE_GROUP);
		    
		    return modelMap;
		}

    
	public Map<String, Object> getAllResidences(int pageNum) {
	    int currentPage = pageNum;
	    int startRow = (currentPage - 1) * PAGE_SIZE;

	    int listCount = accommodationSearchMapper.accommodationCount();

	    // 숙소 목록 가져오기, pageSize를 전달
	    List<ResidenceSearch> searchList = accommodationSearchMapper.findAllResidences(startRow, PAGE_SIZE);

	    int pageCount = listCount / PAGE_SIZE + (listCount % PAGE_SIZE == 0 ? 0 : 1);
	    int startPage = (currentPage / PAGE_GROUP) * PAGE_GROUP + 1 - (currentPage % PAGE_GROUP == 0 ? PAGE_GROUP : 0);
	    int endPage = startPage + PAGE_GROUP - 1;

	    if (endPage > pageCount) {
	        endPage = pageCount;
	    }

	    Map<String, Object> modelMap = new HashMap<>();
	    modelMap.put("results", searchList);
	    modelMap.put("pageCount", pageCount);
	    modelMap.put("startPage", startPage);
	    modelMap.put("endPage", endPage);
	    modelMap.put("currentPage", currentPage);
	    modelMap.put("listCount", listCount);
	    modelMap.put("pageGroup", PAGE_GROUP);
	    return modelMap;
	}

}
