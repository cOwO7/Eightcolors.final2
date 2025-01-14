package com.springbootfinal.app.domain.residence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
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
    /*private LocalDate checkinDate;
    private LocalDate checkoutDate;*/
    private Date checkinDate; // 체크인 날짜
    private Date checkoutDate; // 체크아웃 날짜

    // 조인 필드
    @Getter
    @Setter
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

}
