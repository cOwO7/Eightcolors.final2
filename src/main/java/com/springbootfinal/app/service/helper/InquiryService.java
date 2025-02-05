package com.springbootfinal.app.service.helper;

import com.springbootfinal.app.domain.helper.AnswerDto;
import com.springbootfinal.app.domain.helper.InquiryDto;
import com.springbootfinal.app.mapper.helper.InquiriesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InquiryService {

    @Autowired
    private InquiriesMapper inquiryMapper;

    public List<InquiryDto> getAllInquiries() {
        return inquiryMapper.selectAllInquiries();
    }

    public InquiryDto getInquiryById(Long inquiryNo) {
        return inquiryMapper.selectInquiryById(inquiryNo);
    }

    public void createInquiry(InquiryDto inquiry) {
        inquiryMapper.insertInquiry(inquiry);
    }

    public void updateInquiry(InquiryDto inquiry) {
        inquiryMapper.updateInquiry(inquiry);
    }

    public void deleteInquiry(Long inquiryNo) {
        inquiryMapper.deleteInquiry(inquiryNo);
    }

    public void updateInquiryStatus(Long inquiryNo, String status) {
        inquiryMapper.updateStatus(inquiryNo, status);
    }
}

