package com.springbootfinal.app.controller.residence;

import com.springbootfinal.app.domain.residence.PropertyPhotosDto;
import com.springbootfinal.app.domain.residence.ResidenceDto;
import com.springbootfinal.app.service.residence.ResidenceService;
import com.springbootfinal.app.service.residence.PropertyPhotosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
//@RequestMapping("/residence")
public class ResidenceController {

    private final ResidenceService residenceService;
    private final PropertyPhotosService propertyPhotosService;

    @Autowired
    public ResidenceController(ResidenceService residenceService, PropertyPhotosService propertyPhotosService) {
        this.residenceService = residenceService;
        this.propertyPhotosService = propertyPhotosService;
    }

    // 숙소 목록 페이지
    @GetMapping("/Residence")
    public String getAllResidences(Model model) {
        List<ResidenceDto> residences = residenceService.getAllResidences();
        model.addAttribute("residences", residences);
        return "views/residence/Residence"; // Residence.html 뷰로 이동
    }

    // 숙소 등록 페이지
    @GetMapping("/add")
    public String showAddResidenceForm(Model model) {
        model.addAttribute("residence", new ResidenceDto());
        return "views/residence/ResidenceWriter"; // ResidenceWriter.html 뷰로 이동
    }

    // 숙소 등록 처리
    @PostMapping("/add")
    public String addResidence(@ModelAttribute ResidenceDto residenceDto) {
        residenceService.addResidence(residenceDto);
        return "redirect:/residence/Residence"; // 등록 후 숙소 목록으로 리다이렉트
    }

    // 숙소 상세 정보 페이지
    @GetMapping("/{residNo}")
    public String getResidenceDetails(@PathVariable Long residNo, Model model) {
        ResidenceDto residence = residenceService.getResidenceById(residNo);
        List<PropertyPhotosDto> photos = propertyPhotosService.getPhotosByResidenceId(residNo);
        model.addAttribute("residence", residence);
        model.addAttribute("photos", photos);
        return "views/residence/ResidenceDetail"; // ResidenceDetail.html 뷰로 이동
    }

    // 숙소 사진 추가
    @PostMapping("/{residNo}/add-photo")
    public String addPhoto(@PathVariable Long residNo, @ModelAttribute PropertyPhotosDto propertyPhotosDto) {
        propertyPhotosDto.setResidNo(residNo);
        propertyPhotosService.addPropertyPhoto(propertyPhotosDto);
        return "redirect:/residence/" + residNo; // 사진 추가 후 해당 숙소의 상세 페이지로 리다이렉트
    }

    // 숙소 삭제
    @PostMapping("/{residNo}/delete")
    public String deleteResidence(@PathVariable Long residNo) {
        propertyPhotosService.deletePhotosByResidenceId(residNo); // 숙소와 관련된 사진 삭제
        residenceService.deleteResidence(residNo); // 숙소 삭제
        return "redirect:/residence/Residence"; // 숙소 목록 페이지로 리다이렉트
    }
}