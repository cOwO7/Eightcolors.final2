package com.springbootfinal.app.controller.helper;

import com.springbootfinal.app.domain.helper.AnswerDto;
import com.springbootfinal.app.domain.helper.InquiryDto;
import com.springbootfinal.app.service.helper.AnswerService;
import com.springbootfinal.app.service.helper.InquiryService;
import com.springbootfinal.app.service.login.UserService;
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
    @Autowired
    private UserService userService;

    /**
     * 문의 목록 페이지
     */
    @GetMapping("/inquiries")
    public String listInquiries(Model model) {
        List<InquiryDto> inquiries = inquiryService.getAllInquiries();
        model.addAttribute("inquiries", inquiries);
        return "views/helper/helper";
    }

    // 문의 상세 페이지
    @GetMapping("/inquiries/{inquiryNo}")
    public String viewInquiry(@PathVariable Long inquiryNo,
                              Model model) {
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
    public String createInquiryForm(Model model,
                                    @SessionAttribute("userNo") Long userNo) {
        // userNo로 이름 조회
        String userName = userService.getUserNameByUserNo(userNo);
        // 조회된 이름을 모델에 추가
        model.addAttribute("userName", userName);
        // 새 InquiryDto 객체를 모델에 추가
        model.addAttribute("inquiry", new InquiryDto());
        return "views/helper/InquiriesWriter";  // 작성 페이지로 이동
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


    // 문의 수정 폼
    @GetMapping("/inquiries/{inquiryNo}/update")
    public String updateInquiryForm(@PathVariable Long inquiryNo, Model model,
                                    @SessionAttribute(value = "userNo", required = false) Long sessionUserNo,
                                    @SessionAttribute("role") String sessionRole) {
        InquiryDto inquiry = inquiryService.getInquiryById(inquiryNo);
        // 관리자일 경우나, 본인이 작성한 글일 경우 수정 가능
        if (sessionRole.equals("admin") || (sessionUserNo != null && inquiry.getUserNo().equals(sessionUserNo))) {
            model.addAttribute("inquiry", inquiry);
            return "views/helper/InquiriesUpdate";  // 수정 뷰로 이동
        }

        // 본인이 작성한 글도 아니고, 관리자도 아니면 목록으로 리다이렉트
        return "redirect:/inquiries";
    }


    // 문의 수정 처리
    @PostMapping("/inquiries/{inquiryNo}/update")
    public String updateInquiry(@PathVariable Long inquiryNo,
                                @ModelAttribute InquiryDto inquiry) {
        inquiry.setInquiryNo(inquiryNo);
        inquiryService.updateInquiry(inquiry);
        return "redirect:/inquiries/{inquiryNo}";
    }

    /**
     * 문의 삭제 처리
     */
    @PostMapping("/inquiries/{inquiryNo}/delete")
    public String deleteInquiry(@PathVariable Long inquiryNo,
                                @SessionAttribute(value = "userNo", required = false) Long sessionUserNo,  // userNo가 없을 수도 있음
                                @SessionAttribute("role") String sessionRole) {  // role로 관리자 체크
        InquiryDto inquiry = inquiryService.getInquiryById(inquiryNo);
        // sessionUserNo가 없으면 관리자 role로 체크
        if (sessionRole.equals("admin") || (sessionUserNo != null && inquiry.getUserNo().equals(sessionUserNo))) {
            inquiryService.deleteInquiry(inquiryNo);

            // 관리자 또는 본인이 작성한 글일 때 삭제 후 목록으로 리다이렉트
            return "redirect:/inquiries";
        }
        // 본인이 작성한 글도 아니고, 관리자도 아니면 삭제 불가
        return "redirect:/inquiries";  // 목록으로 리다이렉트
    }

    // 답변 추가
    @PostMapping("/inquiries/{inquiryNo}/answer")
    public String addAnswer(@PathVariable Long inquiryNo,
                            @RequestParam String content,
                            @SessionAttribute("adminName") String adminName,  // 세션에서 adminName 가져오기
                            Model model) {
        // AnswerDto 객체 생성 및 값 설정
        AnswerDto answer = new AnswerDto();
        answer.setInquiryNo(inquiryNo);
        answer.setContent(content);
        answer.setAdminName(adminName);  // 세션에서 가져온 관리자 이름 설정

        // 답변 추가 서비스 호출
        //answerService.addAnswer(answer);
        answerService.addAnswer(answer);
        inquiryService.updateInquiryStatus(inquiryNo, "답변 완료");

        // 모델에 답변 추가
        model.addAttribute("answer", answer);

        // 답변이 등록된 후 원래 문의 상세 페이지로 리디렉션
        return "redirect:/inquiries/{inquiryNo}";
    }
}
