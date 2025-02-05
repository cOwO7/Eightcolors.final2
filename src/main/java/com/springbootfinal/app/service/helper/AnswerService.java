package com.springbootfinal.app.service.helper;

import com.springbootfinal.app.domain.helper.AnswerDto;
import com.springbootfinal.app.mapper.helper.AnswersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerService {

    @Autowired
    private AnswersMapper answerMapper;

    public void addAnswer(Long id, String content) {
        answerMapper.insertAnswer(id, content);
    }

    // 답변 조회
    public List<AnswerDto> getAnswersByInquiryId(Long inquiryId) {
        return answerMapper.allAnswerByInquiryId(inquiryId);
    }
    public List<AnswerDto> getAnswersByInquiryIds(Long inquiryId) {
        return answerMapper.allAnswerByInquiryIds(inquiryId);
    }

    public void deleteAnswer(Long answerNo) {
        answerMapper.deleteAnswer(answerNo);
    }

    public void updateStatus(Long inquiryNo, String status) {
        answerMapper.updateStatus(inquiryNo, status);
    }
}

