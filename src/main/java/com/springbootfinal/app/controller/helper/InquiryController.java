package com.springbootfinal.app.controller.helper;

import com.springbootfinal.app.domain.helper.InquiryDto;
import com.springbootfinal.app.service.helper.InquiryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inquiries")
public class InquiryController {

    private final InquiryService inquiryService;

    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    // 문의 등록
    @PostMapping
    public ResponseEntity<String> createInquiry(@RequestBody InquiryDto inquiryDto) {
        inquiryService.addInquiry(inquiryDto);
        return ResponseEntity.ok("문의가 등록되었습니다.");
    }

    // 모든 문의 가져오기 (관리자 전용)
    /*@GetMapping
    public ResponseEntity<List<InquiryDto>> getAllInquiries(
            @AuthenticationPrincipal UserDetails userDetails) {
        // 관리자만 접근 가능
        if (!userDetails.getAuthorities().stream().anyMatch(
                auth -> auth.getAuthority()
                        .equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(inquiryService.getAllInquiries(userDetails));
    }*/
    @GetMapping("/inquiries")
    public ResponseEntity<List<InquiryDto>> getAllInquiries(@AuthenticationPrincipal UserDetails userDetails,
                                                            Model model) {
        List<InquiryDto> inquiryBList = inquiryService.getAllInquiries(userDetails);
        // 관리자만 접근 가능
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("inquiryBList", inquiryBList);
        if (isAdmin) {
            // 관리자는 모든 문의글을 반환
            return ResponseEntity.ok(inquiryService.getAllInquiries(userDetails));
        } else {
            // 일반 사용자는 자기 자신이 작성한 문의만 반환
            return ResponseEntity.ok(inquiryService.getInquiriesByUser(userDetails));
        }

    }


    // 특정 문의 가져오기 (관리자 전용)
    @GetMapping("/{id}")
    public ResponseEntity<InquiryDto> getInquiryById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        InquiryDto inquiry = inquiryService.getInquiryById(id, userDetails);
        // 작성자 또는 관리자 확인
        boolean isOwner = inquiry.getUserId().equals(userDetails.getUsername());
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isOwner && !isAdmin) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(inquiryService.getInquiryById(id, userDetails));

    }

    // 특정 문의와 답변 조회
    @GetMapping("/{id}/answers")
    public ResponseEntity<Map<String, Object>> getInquiryWithAnswer(@PathVariable Long id) {
        Map<String, Object> inquiryWithAnswer = inquiryService.getInquiryWithAnswer(id);
        return ResponseEntity.ok(inquiryWithAnswer);
    }

    // 문의 상태 업데이트
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateInquiryStatus(@PathVariable Long id, @RequestBody Map<String, String> status, @AuthenticationPrincipal UserDetails userDetails) {
        inquiryService.updateInquiryStatus(id, status.get("status"), userDetails);
        return ResponseEntity.ok("문의 상태가 업데이트되었습니다.");
    }
}

