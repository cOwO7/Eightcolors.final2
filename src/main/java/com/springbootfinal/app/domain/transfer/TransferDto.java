package com.springbootfinal.app.domain.transfer;

import com.springbootfinal.app.domain.reservations.Reservations;
import lombok.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferDto {

    private Long transferNo;
    private String transferTitle;             // 글 제목
    private Long sellerUserNo;
    private Long buyerUserNo;
    private Long reservationNo;
    private BigDecimal transferPrice;
    private String status;                    // 상태
    private LocalDateTime createdAt;
    private String transferContent;           // 글 내용

    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    private String sellerId;                  // 양도자 아이디
    private String buyerId;                   // 양수자 아이디
    private String reservationResidName;      // 예약한 숙소명 (residence 테이블의 resid_name)
    private String roomNo;                    // 예약한 방 번호

    private Reservations reservation; // 예약 정보 추가


    // 포맷팅된 가격을 반환하는 메서드 추가
    public String getFormattedTransferPrice() {
        if (transferPrice == null) {
            return "0 원"; // 혹은 "금액 없음"
        }
        DecimalFormat format = new DecimalFormat("#,###");
        return format.format(transferPrice) + " 원";
    }

    public String getFormattedCreatedAt() {
        if (createdAt == null) {
            return "날짜 없음";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return createdAt.format(formatter);
    }

}