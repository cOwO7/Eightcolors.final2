package com.springbootfinal.app.domain;

import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    private String guestName;      // 손님 이름
    private String guestPhone;      // 손님 전화번호
    private String roomName;       // 방 이름
    private LocalDate checkinDate; // 체크인 날짜
    private LocalDate checkoutDate;// 체크아웃 날짜
    private String reservationStatus; // 예약 상태

}
