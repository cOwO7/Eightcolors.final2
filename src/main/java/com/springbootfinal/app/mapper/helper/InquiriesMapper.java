package com.springbootfinal.app.mapper.helper;

import com.springbootfinal.app.domain.helper.InquiryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface InquiriesMapper {

    void insertInquiry(InquiryDto inquiryDto);

    List<InquiryDto> getAllInquiries();

    InquiryDto getInquiryById(Long inquiryNo);

    Map<String, Object> getInquiryWithUser(@Param("inquiryNo") Long inquiryNo);

    Map<String, Object> getInquiryWithAnswer(@Param("inquiryNo") Long inquiryNo);

    void updateInquiryStatus(@Param("inquiryNo") Long inquiryNo, @Param("status") String status);
}

