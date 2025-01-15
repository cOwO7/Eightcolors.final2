package com.springbootfinal.app.controller.residence;

import com.springbootfinal.app.domain.residence.ResidenceDto;
import com.springbootfinal.app.service.residence.ResidenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
//@RequestMapping("/residence")
public class ResidenceController {

    @Autowired
    private ResidenceService residenceService;

    // 숙소 목록 조회
    @GetMapping("/list")
    public String listResidences(Model model) {
        List<ResidenceDto> residences = residenceService.getAllResidences();

        residences.forEach(residence -> {
            log.info("Residence: {} - Discount Rate: {} - Discounted Price: {}",
                    residence.getResidName(),
                    residence.getDiscountRate(),
                    residence.getDiscountedPrice());
        });

        model.addAttribute("residences", residences);
        return "views/residence/Residence"; // "Residence" 페이지에 데이터를 전달
    }

    // 숙소 상세 조회
    @GetMapping("/detail/{residNo}")
    public String viewResidence(@PathVariable Long residNo, Model model) {
        ResidenceDto residence = residenceService.getResidenceById(residNo);
        model.addAttribute("residence", residence); // "residence" 이름으로 데이터 전달
        return "views/residence/ResidenceDetail"; // 상세보기 페이지로 이동
    }

    // 숙소 등록 페이지
    @GetMapping("/new")
    public String newResidenceForm(Model model) {
        model.addAttribute("residence", new ResidenceDto());
        return "views/residence/ResidenceWriter"; // 숙소 등록 페이지
    }

    // 숙소 등록 처리
    @PostMapping("/new")
    public String createResidence(@ModelAttribute ResidenceDto residence) {
        residenceService.createResidence(residence); // 숙소 등록 로직
        return "redirect:/residence/list"; // 등록 후 목록 페이지로 리다이렉트
    }

    // 숙소 수정 페이지
    @GetMapping("/edit/{residNo}")
    public String editResidenceForm(@PathVariable Long residNo, Model model) {
        ResidenceDto residence = residenceService.getResidenceById(residNo);
        model.addAttribute("residence", residence); // 수정할 숙소 데이터 전달
        return "views/residence/ResidenceUpdata"; // 숙소 수정 페이지
    }

    // 숙소 수정 처리
    @PostMapping("/edit/{residNo}")
    public String updateResidence(@PathVariable Long residNo, @ModelAttribute ResidenceDto residence) {
        residence.setResidNo(residNo); // 숙소 번호 설정
        residenceService.updateResidence(residence); // 숙소 수정
        return "redirect:/residence/list"; // 수정 후 목록 페이지로 리다이렉트
    }

    // 숙소 삭제
    @PostMapping("/delete/{residNo}")
    public String deleteResidence(@PathVariable Long residNo) {
        residenceService.deleteResidence(residNo); // 숙소 삭제
        return "redirect:/residence/list"; // 삭제 후 목록 페이지로 리다이렉트
    }

    // 숙소 매진 상태 갱신
    @PutMapping("/{residNo}/sold-out")
    public ResponseEntity<String> updateSoldOutStatus(@PathVariable Long residNo, @RequestParam boolean soldOut) {
        String message = soldOut ? "숙소가 매진 처리되었습니다." : "숙소 매진 상태가 해제되었습니다.";
        return ResponseEntity.ok(message);
    }
}
