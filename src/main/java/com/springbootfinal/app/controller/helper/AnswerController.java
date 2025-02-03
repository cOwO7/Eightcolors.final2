/*package com.springbootfinal.app.controller.helper;

import com.springbootfinal.app.domain.helper.AnswerDto;
import com.springbootfinal.app.service.helper.AnswerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/answers")
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    // 답변 등록
    @PostMapping
    public ResponseEntity<String> createAnswer(@RequestBody AnswerDto answerDto,
                                               @AuthenticationPrincipal UserDetails userDetails,
                                               HttpSession httpSession,
                                               Model model) {
        answerDto.setAdminUserNo((Long)httpSession.getAttribute("adminUserNo"));
        answerService.addAnswer(answerDto);  // 답변 등록
        model.addAttribute("answer", answerDto);
        return ResponseEntity.ok("답변이 등록되었습니다.");
    }*/

    // 특정 문의에 대한 답변 조회
    /*@GetMapping("/{inquiryId}")
    public ResponseEntity<AnswerDto> getAnswerByInquiryId(@PathVariable Long inquiryId) {
        return ResponseEntity.ok(answerService.getAnswerByInquiryId(inquiryId));
    }
}*/

