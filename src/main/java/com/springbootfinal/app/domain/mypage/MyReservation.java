package com.springbootfinal.app.domain.mypage;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MyReservation {
    private Long reservationNo;
    private Long userNo;
    private Long roomNo;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private int totalPrice;
    private int discountRate;
    private int discountedPrice;
    private String paymentStatus;
    private String reservationStatus;
    private String roomName;
    private String thumbnailUrls;
    private String photoUrl01;
    private String roomUrl01;
    private String residName;
    private String residDescription;
    private String hostName;
    private String hostPhone;

    // Method to get full image path
    public String getFullRoomUrl01() {
        return "/images/files/" + this.roomUrl01;
    }
}