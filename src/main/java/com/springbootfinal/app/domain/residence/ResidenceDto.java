package com.springbootfinal.app.domain.residence;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResidenceDto {

    private Long residNo;
    private String residName;
    private String residDescription;
    private String residAddress;
    private String residType;  // Enum 대신 String으로 저장
    private Date residDate;  // TIMESTAMP는 String으로 매핑
    private boolean soldOut; // 매진 여부 추가
    private String hostId; // 호스트 이름
    private String hostUserName;
    private Long hostUserNo;

    // 공실률 관련 데이터
    private Number totalRooms;
    private Number occupiedRooms;
    private Number availableRooms;
    private Number vacancyRate;


    // 예약 페이지 데이터
    private BigDecimal discountRate; // 할인율
    private BigDecimal discountedPrice; // 할인된 가격
    private Date checkinDate; // 체크인 날짜
    private Date checkoutDate; // 체크아웃 날짜

    // 날씨 데이터
    private int nx;
    private int ny;
    private String latitudeNum;
    private String longitudeNum;
    private String regId;
    private String regIdTemp;

    // 사진 데이터
    private PropertyPhotosDto propertyPhotosDto;  // PropertyPhotosDto 객체
    private String photoUrl01;
    private String photoUrl02;
    private String photoUrl03;
    private String photoUrl04;
    private String photoUrl05;
    private String photoUrl06;
    private String photoUrl07;
    private String photoUrl08;
    private String photoUrl09;
    private String photoUrl10;
    private String thumbnailUrls;
    private List<String> newPhotoUrls;

    // 숙소 방 데이터
    private List<ResidenceRoom> rooms; // ResidenceRoom을 리스트로 추가
    private Long roomNo;
    private String roomName;
    private int pricePerNight;

    private List<ReservationDayDto> reservations; // Reservation 객체의 리스트
    private List<PropertyPhotosDto> propertyPhotos; // residencePhoto 객체 리스트

}
