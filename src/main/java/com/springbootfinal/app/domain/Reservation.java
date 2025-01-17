package com.springbootfinal.app.domain;

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
    private int roomNo;
    private int userNo;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private int totalprice;
    private int discountRate;
    private int discountedPrice;
    private String transactionId;
}
