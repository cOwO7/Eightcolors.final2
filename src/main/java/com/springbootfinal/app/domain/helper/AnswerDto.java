package com.springbootfinal.app.domain.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {
    private Long answerNo;       // 답변 번호
    private Long inquiryNo;      // 문의 번호
    private String adminName;    // 관리자 이름 (추가)
    private String content;      // 답변 내용
    private LocalDateTime answerDate; // 답변 작성일

}
