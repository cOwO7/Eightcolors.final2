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

    public void addAnswer(AnswerDto answer) {
        answerMapper.insertAnswer(answer);
    }
}

