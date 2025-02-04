package com.springbootfinal.app.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
    public String accomSearch(Model model, @RequestParam(value="pageNum", required=false, defaultValue="1") int pageNum) {
        // "modelMap"이 없으면 기본적으로 모든 숙소를 가져옴
        Map<String, Object> modelMap = (Map<String, Object>) model.asMap().get("modelMap");

        if (modelMap == null) {
            modelMap = accommodationSearchService.getAllResidences(pageNum);
        }

        model.addAllAttributes(modelMap); // 모델에 데이터 추가
        return "accommodationSearch"; // 숙소 검색 페이지로 반환
    }


    @GetMapping("/search")
    public String searchResidences(
            @RequestParam(name = "searchKeyword", required = false) String searchKeyword,
            @RequestParam(name = "checkinDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkinDate,
            @RequestParam(name = "checkoutDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkoutDate,
            @RequestParam(name = "accommodationType", required = false) List<String> accommodationTypes, // 체크된 타입 리스트 받기
            Model model,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
            RedirectAttributes redirectAttributes
    ) {
        // 숙소 검색 결과 조회
        Map<String, Object> modelMap = accommodationSearchService.getAvailableResidences(searchKeyword, checkinDate, checkoutDate, pageNum,accommodationTypes);

        // RedirectAttributes에 모델 추가
        model.addAllAttributes(modelMap);

        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("checkinDate", checkinDate);
        model.addAttribute("checkoutDate", checkoutDate);
        model.addAttribute("accommodationTypes", accommodationTypes); // accommodationTypes를 모델에 추가

        return "accommodationSearch"; // 숙소 검색 페이지로 반환
    }




    @GetMapping("/test")
    public String test(){
        return "test";
    }






}
