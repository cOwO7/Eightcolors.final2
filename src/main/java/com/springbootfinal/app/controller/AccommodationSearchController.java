package com.springbootfinal.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    public String accomSearch(){

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
