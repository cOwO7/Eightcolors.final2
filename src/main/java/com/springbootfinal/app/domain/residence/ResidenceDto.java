package com.springbootfinal.app.domain.residence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
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

    // 업자 No 데이터
    private Long hostUserNo;

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


    // totalPrice를 계산하는 메소드 추가
    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = BigDecimal.ZERO;
        if (rooms != null) {
            for (ResidenceRoom room : rooms) {
                totalPrice = totalPrice.add(BigDecimal.valueOf(room.getPricePerNight()));
            }
        }
        return totalPrice;
    }


    // log.info toString 메서드
    @Override
    public String toString() {
        return "ResidenceDto{" +
                "residNo=" + residNo +
                ", residName='" + residName + '\'' +
                ", residDescription='" + residDescription + '\'' +
                ", residAddress='" + residAddress + '\'' +
                ", residType='" + residType + '\'' +
                ", residDate=" + residDate +
                ", discountRate=" + discountRate +
                ", discountedPrice=" + discountedPrice +
                ", checkinDate=" + checkinDate +
                ", checkoutDate=" + checkoutDate +
                ", soldOut=" + soldOut +
                ", nx=" + nx +
                ", ny=" + ny +
                ", latitudeNum=" + latitudeNum +
                ", longitudeNum =" + longitudeNum  +
                ", regId='" + regId + '\'' +
                ", regIdTemp='" + regIdTemp + '\'' +
                ", hostUserNo=" + hostUserNo + '\'' +
                ", pUrl01='" + photoUrl01 + '\'' +
                ", pUrl02='" + photoUrl02 + '\'' +
                ", pUrl03='" + photoUrl03 + '\'' +
                ", pUrl04='" + photoUrl04 + '\'' +
                ", pUrl05='" + photoUrl05 + '\'' +
                ", pUrl06='" + photoUrl06 + '\'' +
                ", pUrl07='" + photoUrl07 + '\'' +
                ", pUrl08='" + photoUrl08 + '\'' +
                ", pUrl09='" + photoUrl09 + '\'' +
                ", pUrl10='" + photoUrl10 + '\'' +
                ", tUrls='" + thumbnailUrls + '\'' +
                '}';
    }

}
