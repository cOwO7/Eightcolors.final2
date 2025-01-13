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
public class InquiryDto {
	private LocalDateTime inquiryDate;
	private Long inquiryNo;        // 문의 번호
	private Long userNo;           // 작성자 번호 (FK)
	private String userId;         // 작성자 ID (추가)
	private String userName;       // 작성자 이름 (추가)
	private String title;          // 문의 제목
	private String content;        // 문의 내용
	private String status;         // 문의 상태
	private String answerContent;  // 답변 내용 (추가)
	private String adminId;        // 답변 작성자 ID (추가)
	private String adminName;      // 답변 작성자 이름 (추가)
}
