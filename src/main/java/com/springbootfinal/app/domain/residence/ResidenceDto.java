package com.springbootfinal.app.domain.residence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
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
    private BigDecimal discountRate; // 할인율
    private BigDecimal discountedPrice; // 할인된 가격
    private Date checkinDate; // 체크인 날짜
    private Date checkoutDate; // 체크아웃 날짜
    private boolean soldOut; // 매진 여부 추가

    // 날씨 데이터
    private int nx;
    private int ny;
    private String latitudeNum;
    private String longitudeNum;
    private String regId;
    private String regIdTemp;

    public String getLatitudeNum() {
        return latitudeNum;
    }

    public void setLatitudeNum(String latitudeNum) {
        this.latitudeNum = latitudeNum;
    }

    public String getLongitudeNum() {
        return longitudeNum;
    }

    public void setLongitudeNum(String longitudeNum) {
        this.longitudeNum = longitudeNum;
    }

    // 업자 No 데이터
    private Long hostUserNo;

    // 사진 데이터
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



    public void setPhotoUrls(List<String> photoUrls) {
        int size = photoUrls.size();
        this.photoUrl01 = size > 0 ? photoUrls.get(0) : null;
        this.photoUrl02 = size > 1 ? photoUrls.get(1) : null;
        this.photoUrl03 = size > 2 ? photoUrls.get(2) : null;
        this.photoUrl04 = size > 3 ? photoUrls.get(3) : null;
        this.photoUrl05 = size > 4 ? photoUrls.get(4) : null;
        this.photoUrl06 = size > 5 ? photoUrls.get(5) : null;
        this.photoUrl07 = size > 6 ? photoUrls.get(6) : null;
        this.photoUrl08 = size > 7 ? photoUrls.get(7) : null;
        this.photoUrl09 = size > 8 ? photoUrls.get(8) : null;
        this.photoUrl10 = size > 9 ? photoUrls.get(9) : null;
    }
    // 모든 사진 URL을 리스트로 반환하는 메소드
    public List<String> getAllPhotoUrls() {
        List<String> photoUrls = new ArrayList<>();
        if (photoUrl01 != null) photoUrls.add(photoUrl01);
        if (photoUrl02 != null) photoUrls.add(photoUrl02);
        if (photoUrl03 != null) photoUrls.add(photoUrl03);
        if (photoUrl04 != null) photoUrls.add(photoUrl04);
        if (photoUrl05 != null) photoUrls.add(photoUrl05);
        if (photoUrl06 != null) photoUrls.add(photoUrl06);
        if (photoUrl07 != null) photoUrls.add(photoUrl07);
        if (photoUrl08 != null) photoUrls.add(photoUrl08);
        if (photoUrl09 != null) photoUrls.add(photoUrl09);
        if (photoUrl10 != null) photoUrls.add(photoUrl10);
        return photoUrls;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrls = thumbnailUrl;
    }

    private PropertyPhotosDto propertyPhotosDto;  // PropertyPhotosDto 객체

    // 조인 필드
    private List<ReservationDto> reservations; // Reservation 객체의 리스트
    private List<String> propertyPhotos; // residencePhoto 객체 리스트
    private List<ResidenceRoom> rooms; // ResidenceRoom을 리스트로 추가

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
