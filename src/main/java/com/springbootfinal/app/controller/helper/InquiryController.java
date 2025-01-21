package com.springbootfinal.app.controller.helper;

import com.springbootfinal.app.domain.helper.AnswerDto;
import com.springbootfinal.app.domain.helper.InquiryDto;
import com.springbootfinal.app.service.helper.AnswerService;
import com.springbootfinal.app.service.helper.InquiryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class InquiryController {

    @Autowired
    private InquiryService inquiryService;

    @Autowired
    private AnswerService answerService;

    // 문의 목록 페이지
    @GetMapping("/inquiries")
    public String listInquiries(Model model) {
        List<InquiryDto> inquiries = inquiryService.getAllInquiries();
        model.addAttribute("inquiries", inquiries);
        return "views/helper";
    }
    // 문의 상세 페이지
    @GetMapping("/inquiries/{inquiryNo}")
    public String viewInquiry(@PathVariable Long inquiryNo, Model model) {
        InquiryDto inquiry = inquiryService.getInquiryById(inquiryNo);
        model.addAttribute("inquiry", inquiry);
        return "views/InquiriesDetail";
    }

    // 문의 작성 폼
    @GetMapping("/inquiries/create")
    public String createInquiryForm(Model model) {
        model.addAttribute("inquiry", new InquiryDto());
        return "views/InquiriesWriter";
    }

    // 문의 등록 처리
    @PostMapping("/inquiries")
    public String createInquiry(@ModelAttribute InquiryDto inquiry) {
        inquiryService.createInquiry(inquiry);
        return "redirect:/inquiries";
    }

    // 문의 수정 폼
    @GetMapping("/inquiries/{inquiryNo}/edit")
    public String editInquiryForm(@PathVariable Long inquiryNo, Model model) {
        InquiryDto inquiry = inquiryService.getInquiryById(inquiryNo);
        model.addAttribute("inquiry", inquiry);
        return "views/InquiriesUpdate";
    }

    // 문의 수정 처리
    @PostMapping("/inquiries/{inquiryNo}")
    public String editInquiry(@PathVariable Long inquiryNo, @ModelAttribute InquiryDto inquiry) {
        inquiry.setInquiryNo(inquiryNo);
        inquiryService.updateInquiry(inquiry);
        return "redirect:/inquiries/{inquiryNo}";
    }

    // 문의 삭제 처리
    @PostMapping("/inquiries/{inquiryNo}/delete")
    public String deleteInquiry(@PathVariable Long inquiryNo) {
        inquiryService.deleteInquiry(inquiryNo);
        return "redirect:/inquiries";
    }

    // 답변 추가
    @PostMapping("/inquiries/{inquiryNo}/answer")
    public String addAnswer(@PathVariable Long inquiryNo, @RequestParam String content) {
        AnswerDto answer = new AnswerDto();
        answer.setInquiryNo(inquiryNo);
        answer.setContent(content);
        log.info("답변 전송: {}", answer);
        // 관리자 번호는 로그인 정보에서 가져와야 합니다.
        answer.setAdminUserNo(1L);  // 예시로 관리자 번호를 1로 설정
        answerService.addAnswer(answer);
        return "redirect:/inquiries/{inquiryNo}";
    }
}

