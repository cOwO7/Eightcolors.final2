package com.springbootfinal.app.domain.transfer;

import lombok.Data;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Data
public class TransferDto {

    private Long transferNo;
    private String transferTitle;             // 글 제목
    private Long sellerUserNo;
    private Long buyerUserNo;
    private Long reservationNo;
    private BigDecimal transferPrice;
    private String status;                    // 상태
    private LocalDateTime createdAt;

    private String sellerId;                  // 양도자 아이디
    private String buyerId;                   // 양수자 아이디
    private String reservationResidName;      // 예약한 숙소명 (residence 테이블의 resid_name)

    // 포맷팅된 가격을 반환하는 메서드 추가
    public String getFormattedTransferPrice() {
        DecimalFormat format = new DecimalFormat("#,###");
        return format.format(transferPrice) + " 원";
    }

}
