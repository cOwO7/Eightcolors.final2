package com.springbootfinal.app.mapper.helper;

import com.springbootfinal.app.domain.helper.AnswerDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AnswersMapper {

    void insertAnswer(AnswerDto answerDto);

    AnswerDto getAnswerByInquiryId(Long inquiryNo);

    List<AnswerDto> getAnswersByInquiryId(Long inquiryNo);
}

