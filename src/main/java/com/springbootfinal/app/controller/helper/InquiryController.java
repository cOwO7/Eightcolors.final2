package com.springbootfinal.app.controller.helper;

import com.springbootfinal.app.domain.helper.InquiryDto;
import com.springbootfinal.app.service.helper.InquiryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping
    public ResponseEntity<List<InquiryDto>> getAllInquiries(
            @AuthenticationPrincipal UserDetails userDetails) {
        // 관리자만 접근 가능
        if (!userDetails.getAuthorities().stream().anyMatch(
                auth -> auth.getAuthority()
                        .equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(inquiryService.getAllInquiries(userDetails));
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
}

