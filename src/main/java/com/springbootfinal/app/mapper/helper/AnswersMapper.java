package com.springbootfinal.app.mapper.helper;

import com.springbootfinal.app.domain.helper.AnswerDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AnswersMapper {

    // 답변 추가
    void insertAnswer(AnswerDto answer);
    // 답변 조회
    List<AnswerDto> allAnswerByInquiryId(Long inquiryId);
}