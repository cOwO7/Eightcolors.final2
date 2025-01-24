package com.springbootfinal.app.domain.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    private Long roomNo;
    private Long userNo;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private int totalprice;
    private int discountRate;
    private int discountedPrice;
    private String transactionId;
    private String paymentStatus;
    private LocalDate createdAt;
}
