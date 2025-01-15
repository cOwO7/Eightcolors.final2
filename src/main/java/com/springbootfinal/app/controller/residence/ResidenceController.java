package com.springbootfinal.app.controller.residence;


import com.springbootfinal.app.domain.residence.ResidenceDto;
import com.springbootfinal.app.domain.residence.ResidenceRoom;
import com.springbootfinal.app.service.residence.ResidenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@Slf4j
@Controller
public class ResidenceController {

    @Autowired
    private ResidenceService residenceService;

    // 숙소 목록 조회
    @GetMapping("/list")
    public String listResidences(Model model) {
        // 실제 DB에서 데이터를 가져옴
        List<ResidenceDto> residences = residenceService.getAllResidences();

        residences.forEach(residence -> {
            log.info("Residence: {} - Discount Rate: {} - Discounted Price: {}",
                    residence.getResidName(),
                    residence.getDiscountRate(),
                    residence.getDiscountedPrice());
        });

        // 데이터 모델에 추가
        model.addAttribute("residences", residences);
        log.info("listResidences: {}", residences);

        return "views/residence/Residence";
    }


    // 숙소 상세 조회
    @GetMapping("/{residNo}")
    public String viewResidence(@PathVariable Long residNo, Model model) {
        ResidenceDto residence = residenceService.getResidenceById(residNo);
        model.addAttribute("residences", residence);
        return "views/residence/ResidenceDetail";
    }

    // 숙소 등록 페이지
    @GetMapping("/new")
    public String newResidenceForm(Model model) {
        model.addAttribute("residences", new ResidenceDto());
        return "views/residence/ResidenceWriter";
    }

    // 숙소 등록 처리
    @PostMapping("/new")
    public String createResidence(@ModelAttribute ResidenceDto residence) {
        residenceService.createResidence(residence);
        return "redirect:/Residence";
    }

    // 숙소 수정 페이지
    @GetMapping("/{residNo}/edit")
    public String editResidenceForm(@PathVariable Long residNo, Model model) {
        ResidenceDto residence = residenceService.getResidenceById(residNo);
        model.addAttribute("residences", residence);
        return "views/residence/ResidenceUpdata";
    }

    // 숙소 수정 처리
    @PostMapping("/{residNo}/edit")
    public String updateResidence(@PathVariable Long residNo, @ModelAttribute ResidenceDto residence) {
        residence.setResidNo(residNo);
        residenceService.updateResidence(residence);
        return "redirect:/residence";
    }

    // 숙소 삭제
    @PostMapping("/{residNo}/delete")
    public String deleteResidence(@PathVariable Long residNo) {
        residenceService.deleteResidence(residNo);
        return "redirect:/residence";
    }
}