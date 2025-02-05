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

import com.springbootfinal.app.service.AccommodationSearchService;

@Controller
public class AccommodationSearchController {

    @Autowired
    private AccommodationSearchService accommodationSearchService;

    @GetMapping("/accomSearch")
    public String accomSearch(Model model,
                              @RequestParam(value="pageNum", required=false, defaultValue="1") int pageNum,
                              @RequestParam(name = "accommodationTypes", required = false) List<String> accommodationTypes, // 체크된 타입 리스트 받기
                              @RequestParam(name = "maxPrice", required = false) Integer maxPrice,
                              @RequestParam(name = "searchKeyword", required = false) String searchKeyword, // 검색 키워드 받기
                              @RequestParam(name = "checkinDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkinDate,
                              @RequestParam(name = "checkoutDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkoutDate
    ) {
        // 검색 키워드가 있으면 해당 키워드로 검색 실행
        Map<String, Object> modelMap = accommodationSearchService.getAvailableResidences(searchKeyword, checkinDate, checkoutDate, pageNum, accommodationTypes, maxPrice);

        model.addAttribute("maxPrice", maxPrice); // maxPrice를 모델에 추가
        model.addAttribute("searchKeyword", searchKeyword); // searchKeyword를 모델에 추가
        model.addAttribute("checkinDate", checkinDate); // checkinDate를 모델에 추가
        model.addAttribute("checkoutDate", checkoutDate); // checkoutDate를 모델에 추가

        if (accommodationTypes != null) {
            System.out.println("accommodationTypes: " + String.join(",", accommodationTypes));
            model.addAttribute("accommodationTypes", String.join(",", accommodationTypes));
        }

        model.addAllAttributes(modelMap); // 모델에 데이터 추가
        return "accommodationSearch"; // 숙소 검색 페이지로 반환
    }

    @GetMapping("/search")
    public String searchResidences(
            @RequestParam(name = "searchKeyword", required = false) String searchKeyword,
            @RequestParam(name = "checkinDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkinDate,
            @RequestParam(name = "checkoutDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkoutDate,
            @RequestParam(name = "accommodationTypes", required = false) List<String> accommodationTypes, // 체크된 타입 리스트 받기
            @RequestParam(name = "maxPrice", required = false) Integer maxPrice, // 최대 금액 받기
            Model model,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
            RedirectAttributes redirectAttributes
    ) {
        // 숙소 검색 결과 조회 (maxPrice 추가)
        Map<String, Object> modelMap = accommodationSearchService.getAvailableResidences(
                searchKeyword,
                checkinDate,
                checkoutDate,
                pageNum,
                accommodationTypes,
                maxPrice); // maxPrice를 서비스 메서드에 추가

        // RedirectAttributes에 모델 추가
        model.addAllAttributes(modelMap);

        if (accommodationTypes != null) {
            System.out.println("accommodationTypes: " + String.join(",", accommodationTypes));
            model.addAttribute("accommodationTypes", String.join(",", accommodationTypes));
        }

        // 모델에 파라미터 추가
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("checkinDate", checkinDate);
        model.addAttribute("checkoutDate", checkoutDate);
        model.addAttribute("maxPrice", maxPrice); // maxPrice를 모델에 추가

        return "accommodationSearch"; // 숙소 검색 페이지로 반환
    }
}