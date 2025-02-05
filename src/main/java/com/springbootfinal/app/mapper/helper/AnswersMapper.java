package com.springbootfinal.app.mapper.helper;

import com.springbootfinal.app.domain.helper.AnswerDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AnswersMapper {

    // 답변 추가
    int insertAnswer(@Param("inquiryNo") Long inquiryNo, @Param("content") String content);

    // 답변 조회
    List<AnswerDto> allAnswerByInquiryId(Long inquiryId);
    List<AnswerDto> allAnswerByInquiryIds(Long inquiryId);


    void deleteAnswer(Long answerNo);

    int updateStatus(@Param("inquiryNo") Long inquiryNo, @Param("status") String status);
}