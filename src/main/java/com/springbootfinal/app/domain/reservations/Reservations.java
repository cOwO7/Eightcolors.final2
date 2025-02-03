package com.springbootfinal.app.domain.reservations;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import java.sql.Date;

@Getter
@Setter
@ToString
@Alias("Reservations")
public class Reservations {

    private Long reservationNo;  // reservation_no mapping
    private Long userNo;         // user_no mapping
    private Long roomNo;
    private String residName; // 숙소명 필드 추가
    private Date checkinDate; // 필드 이름 변경
    private Date checkoutDate; // 필드 이름 변경
    private Integer totalPrice;
    private Integer discountRate;
    private Float discountedPrice;
    private String transactionId;
    private String paymentStatus;
    private String reservationStatus;
    private String createdAt;
    private String updatedAt;

}