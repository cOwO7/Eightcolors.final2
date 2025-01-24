package com.springbootfinal.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccommodationSearchController {

    @GetMapping("/accomSearch")
    public String accomSearch(){

        return "accommodationSearch";
    }




}
