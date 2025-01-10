package com.springbootfinal.app.mapper.helper;

import com.springbootfinal.app.domain.helper.AnswerDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AnswersMapper {

    void insertAnswer(AnswerDto answerDto);

    AnswerDto getAnswerByInquiryId(Long inquiryNo);
}

