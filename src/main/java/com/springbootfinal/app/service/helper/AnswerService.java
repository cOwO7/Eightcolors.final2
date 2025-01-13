package com.springbootfinal.app.service.helper;

import com.springbootfinal.app.domain.helper.AnswerDto;
import com.springbootfinal.app.mapper.helper.AnswersMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerService {

    private final AnswersMapper answersMapper;
    private final InquiryService inquiryService;

    public AnswerService(AnswersMapper answersMapper, InquiryService inquiryService) {
        this.answersMapper = answersMapper;
        this.inquiryService = inquiryService;
    }

    // 답변 등록
    public void addAnswer(AnswerDto answerDto, UserDetails userDetails) {
        // 관리자 정보 세팅
        answerDto.setAdminUserNo(Long.parseLong(userDetails.getUsername())); // 여기서는 UserDetails로부터 관리자의 ID를 가져와야 한다고 가정.

        // 답변 등록
        answersMapper.insertAnswer(answerDto);

        // 문의 상태를 'answered'로 업데이트
        inquiryService.updateInquiryStatus(answerDto.getInquiryNo(), "answered", userDetails);
    }

    // 특정 문의에 대한 답변 조회
    public AnswerDto getAnswerByInquiryId(Long inquiryNo) {
        return answersMapper.getAnswerByInquiryId(inquiryNo);
    }

    public List<AnswerDto> getAnswersByInquiryId(Long inquiryNo) {
        return answersMapper.getAnswersByInquiryId(inquiryNo);
    }
}

