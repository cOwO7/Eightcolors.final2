package com.springbootfinal.app.domain.residence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResidenceDto {

    private Long residNo;               // 숙소 번호 (PK)
    private String residName;           // 숙소 이름
    private String residDescription;    // 숙소 상세 설명
    private String residAddress;        // 숙소 주소
    private String residType;           // 숙소 유형 ('resort', 'hotel', 'pension')
    private Date checkinDate;           // 체크인 날짜
    private Date checkoutDate;          // 체크아웃 날짜
    private BigDecimal totalPrice;      // 원가
    private Integer discountRate;       // 할인율
    private BigDecimal discountedPrice; // 할인된 가격
    private BigDecimal rating;          // 평균 평점
    private Date residDate;             // 등록일 (TIMESTAMP)

}
