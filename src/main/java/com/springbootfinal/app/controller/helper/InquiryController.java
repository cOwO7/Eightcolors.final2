package com.springbootfinal.app.controller.helper;

import com.springbootfinal.app.domain.helper.AnswerDto;
import com.springbootfinal.app.domain.helper.InquiryDto;
import com.springbootfinal.app.service.helper.AnswerService;
import com.springbootfinal.app.service.helper.InquiryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 문의(고객센터) 관련 컨트롤러
 */
@Slf4j
@Controller
public class InquiryController {

    @Autowired
    private InquiryService inquiryService;

    @Autowired
    private AnswerService answerService;

    /**
     * 문의 목록 페이지
     */
    @GetMapping("/inquiries")
    public String listInquiries(Model model) {
        List<InquiryDto> inquiries = inquiryService.getAllInquiries();
        model.addAttribute("inquiries", inquiries);
        return "views/helper/helper";
    }

    /**
     * 문의 상세 페이지
     */
    @GetMapping("/inquiries/{inquiryNo}")
    public String viewInquiry(@PathVariable Long inquiryNo, Model model) {
        InquiryDto inquiry = inquiryService.getInquiryById(inquiryNo);
        List<AnswerDto> answers = answerService.getAnswersByInquiryId(inquiryNo);

        model.addAttribute("inquiry", inquiry);
        model.addAttribute("answers", answers);
        return "views/helper/InquiriesDetail";
    }

    /**
     * 문의 작성 폼
     */
    @GetMapping("/inquiries/create")
    public String createInquiryForm(Model model) {
        // Builder 사용 예시: 빈 객체를 미리 넘길 수도 있지만,
        // 단순 폼에선 굳이 객체 생성 없이도 가능하다.
        model.addAttribute("inquiry", InquiryDto.builder().build());
        return "views/helper/InquiriesWriter";
    }

    /**
     * 문의 등록 처리
     */
    @PostMapping("/inquiries")
    public String createInquiry(@ModelAttribute InquiryDto inquiry) {
        // inquiry도 Builder 사용 가능(현재 @ModelAttribute 받기 때문에 필요 시 내부 Builder 구성)
        inquiryService.createInquiry(inquiry);
        return "redirect:/inquiries";
    }

    /**
     * 문의 수정 폼
     */
    @GetMapping("/inquiries/{inquiryNo}/edit")
    public String editInquiryForm(@PathVariable Long inquiryNo, Model model) {
        InquiryDto inquiry = inquiryService.getInquiryById(inquiryNo);
        model.addAttribute("inquiry", inquiry);
        return "views/helper/InquiriesUpdate";
    }

    /**
     * 문의 수정 처리
     * Setter 대신 Builder로 새로운 객체 생성 후 업데이트 진행
     */
    @PostMapping("/inquiries/{inquiryNo}")
    public String editInquiry(@PathVariable Long inquiryNo,
                              @ModelAttribute InquiryDto inquiry) {

        // 기존 inquiry의 필드들을 Builder로 복사해 새로운 객체 생성
        InquiryDto updated = InquiryDto.builder()
                .inquiryNo(inquiryNo)
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .userId(inquiry.getUserId())
                .status(inquiry.getStatus())
                .inquiryDate(inquiry.getInquiryDate())
                .build();

        inquiryService.updateInquiry(updated);
        return "redirect:/inquiries/{inquiryNo}";
    }

    /**
     * 문의 삭제 처리
     */
    @PostMapping("/inquiries/{inquiryNo}/delete")
    public String deleteInquiry(@PathVariable Long inquiryNo) {
        inquiryService.deleteInquiry(inquiryNo);
        return "redirect:/inquiries";
    }
}
