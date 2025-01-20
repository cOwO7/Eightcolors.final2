package com.springbootfinal.app.domain.residence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {

    private BigDecimal discountRate; // 할인율
    private BigDecimal discountedPrice; // 할인된 가격
    private LocalDate checkinDate; // 체크인 날짜
    private LocalDate checkoutDate; // 체크아웃 날짜
}