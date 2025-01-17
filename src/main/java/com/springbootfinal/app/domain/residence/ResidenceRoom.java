package com.springbootfinal.app.domain.residence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResidenceRoom {

    private Long roomNo;
    private Long residNo;  // 외래 키
    private String roomName;
    private int pricePerNight;
}
