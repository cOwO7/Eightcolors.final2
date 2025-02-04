package com.springbootfinal.app.mapper.helper;

import com.springbootfinal.app.domain.helper.InquiryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface InquiriesMapper {

    // 문의 목록 조회
    List<InquiryDto> selectAllInquiries();

    // 특정 문의 조회
    InquiryDto selectInquiryById(Long inquiryNo);

    // 문의 추가
    void insertInquiry(InquiryDto inquiry);

    // 문의 수정
    void updateInquiry(InquiryDto inquiry);

    // 문의 삭제
    void deleteInquiry(Long inquiryNo);

    // 답변 등록시 status 수정
    void updateStatus(@Param("inquiryNo") Long inquiryNo,
                      @Param("status") String status);
}

