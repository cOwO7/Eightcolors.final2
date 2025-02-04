package com.springbootfinal.app.domain.admin;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class RecentOrderDto {

    private Long reservationNo;
    private String userName;
    private String hostName;
    private String phone;
    private Date checkinDate;
    private BigDecimal discountedPrice;
    private Date createdAt;

    private int totalRooms;
    private int emptyRooms;
    private double occupancyRate;

}
