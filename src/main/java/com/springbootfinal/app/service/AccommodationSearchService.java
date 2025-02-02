package com.springbootfinal.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springbootfinal.app.domain.ResidenceSearch;
import com.springbootfinal.app.mapper.AccommodationSearchMapper;

@Service
public class AccommodationSearchService {
	
	@Autowired
	private AccommodationSearchMapper accommodationSearchMapper;
	
    public List<ResidenceSearch> getAvailableResidences(String checkinDate, String checkoutDate) {
        return accommodationSearchMapper.findAvailableResidences(checkinDate, checkoutDate);
    }
}
