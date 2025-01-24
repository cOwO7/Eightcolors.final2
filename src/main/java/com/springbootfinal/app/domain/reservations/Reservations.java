package com.springbootfinal.app.domain.reservations;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import java.sql.Date;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@Alias("Reservations")
public class Reservations {

    private Long reservation_no;
    private Long user_no;
    private Long room_no;

    private String residName; // 숙소명 필드 추가
    private Date checkin_date; // 필드 이름 변경
    private Date checkout_date; // 필드 이름 변경
    private Date checkinDate; // 필드 이름 변경
    private Date checkoutDate; // 필드 이름 변경
    private Integer total_price;
    private Integer discount_rate;
    private Float discounted_price;
    private String transaction_id;
    private String payment_status;
    private String reservation_status;
    private String created_at;
    private String updated_at;

}
