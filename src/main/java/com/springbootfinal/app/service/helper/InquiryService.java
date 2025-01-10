package com.springbootfinal.app.service.helper;

import com.springbootfinal.app.domain.helper.InquiryDto;
import com.springbootfinal.app.mapper.helper.InquiriesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InquiryService {

    private final InquiriesMapper inquiriesMapper;

    public InquiryService(InquiriesMapper inquiriesMapper) {
        this.inquiriesMapper = inquiriesMapper;
    }

    public Map<String, Object> getInquiryWithUser(Long inquiryNo) {
        return inquiriesMapper.getInquiryWithUser(inquiryNo);
    }

    public Map<String, Object> getInquiryWithAnswer(Long inquiryNo) {
        return inquiriesMapper.getInquiryWithAnswer(inquiryNo);
    }

    // 문의 등록
    public void addInquiry(InquiryDto inquiryDto) {
        inquiryDto.setStatus("pending");
        inquiriesMapper.insertInquiry(inquiryDto);
    }

    // 모든 문의 가져오기 (관리자 전용)
    public List<InquiryDto> getAllInquiries(UserDetails userDetails) {
        // 관리자 권한 확인
        if (!userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority()
                        .equals("ROLE_ADMIN"))) {
            throw new SecurityException("접근 권한이 없습니다.");
        }
        return inquiriesMapper.getAllInquiries();
    }

    // 특정 문의 가져오기 (작성자, 관리자 전용)
    public InquiryDto getInquiryById(Long inquiryNo, UserDetails userDetails) {
        InquiryDto inquiry = inquiriesMapper.getInquiryById(inquiryNo);

        if (inquiry == null) {
            throw new IllegalArgumentException("문의글을 찾을 수 없습니다.");
        }
        // 작성자, 관리자 확인
        boolean isOwner = inquiry.getUserId().equals(userDetails.getUsername());
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority()
                        .equals("RELE_ADMIN"));
        if (!isOwner && !isAdmin) {
            throw new SecurityException("접근 권한이 없습니다.");
        }
        return inquiry;
    }
    // 문의 상태 업데이트
    public void updateInquiryStatus(Long inquiryNo, String status, UserDetails userDetails) {
        // 관리자 권한 확인
        if ((!userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority()
                        .equals("ROLE_ADMIN")))){
            throw new SecurityException("접근 권한이 없습니다.");
        }
        inquiriesMapper.updateInquiryStatus(inquiryNo, status);
    }
}

