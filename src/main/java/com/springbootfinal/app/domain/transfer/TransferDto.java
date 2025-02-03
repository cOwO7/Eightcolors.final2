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
    private String transferTitle;
    private Long sellerUserNo;
    private Long buyerUserNo;
    private Long reservationNo;
    private BigDecimal transferPrice;
    private String status;
    private LocalDateTime createdAt;
    private String transferContent;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String sellerId;
    private String buyerId;
    private String reservationResidName;
    private String roomNo;
    private Reservations reservation;

    // 포맷팅된 가격을 반환하는 메서드 추가
    public String getFormattedTransferPrice() {
        if (transferPrice == null) {
            return "0 원"; // 혹은 "금액 없음"
        }
        DecimalFormat format = new DecimalFormat("#,###");
        return format.format(transferPrice) + " 원";
    }

    // 포맷팅된 생성일시를 반환하는 메서드 추가
    public String getFormattedCreatedAt() {
        if (createdAt == null) {
            return "날짜 없음";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return createdAt.format(formatter);
    }
}