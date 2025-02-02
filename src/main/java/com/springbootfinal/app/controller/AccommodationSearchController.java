package com.springbootfinal.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springbootfinal.app.domain.ResidenceSearch;
import com.springbootfinal.app.service.AccommodationSearchService;

@Controller
public class AccommodationSearchController {
	
	@Autowired
	private AccommodationSearchService accommodationSearchService;

    @GetMapping("/accomSearch")
    public String accomSearch(Model model){
    	  List<ResidenceSearch> residences = (List<ResidenceSearch>) model.asMap().get("results");
    	    
    	    // "results"가 없으면 기본적으로 모든 숙소를 가져옵니다.
    	    if (residences == null) {
    	        residences = accommodationSearchService.getAllResidences();
    	        model.addAttribute("results",residences);
    	    }
        return "accommodationSearch";
    }
    
    @GetMapping("/search")
    public String searchResidences(
        @RequestParam(name = "checkinDate") String checkinDate, 
        @RequestParam(name = "checkoutDate") String checkoutDate,
        RedirectAttributes redirectAttributes
    ) {
        List<ResidenceSearch> results = accommodationSearchService.getAvailableResidences(checkinDate, checkoutDate);
        
        redirectAttributes.addFlashAttribute("results", results);
        redirectAttributes.addFlashAttribute("checkinDate", checkinDate);
        redirectAttributes.addFlashAttribute("checkoutDate", checkoutDate);

        return "redirect:/accomSearch";
    }






}
