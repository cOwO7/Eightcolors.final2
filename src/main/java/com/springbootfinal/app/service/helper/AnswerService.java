package com.springbootfinal.app.service.helper;

import com.springbootfinal.app.domain.helper.AnswerDto;
import com.springbootfinal.app.mapper.helper.AnswersMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {

    private final AnswersMapper answersMapper;
    private final InquiryService inquiryService;

    public AnswerService(AnswersMapper answersMapper, InquiryService inquiryService) {
        this.answersMapper = answersMapper;
        this.inquiryService = inquiryService;
    }

    public void addAnswer(AnswerDto answerDto, UserDetails userDetails) {
        answersMapper.insertAnswer(answerDto);
        inquiryService.updateInquiryStatus(answerDto.getInquiryNo(), "answered", userDetails);

    }

    public AnswerDto getAnswerByInquiryId(Long inquiryNo) {
        return answersMapper.getAnswerByInquiryId(inquiryNo);
    }
}

