package com.springbootfinal.app.domain.reservations;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString
@Alias("Reservations")
public class Reservations {

    private Long reservation_no;
    private Long user_no;
    private Long room_no;
    private String checkin_date;
    private String checkout_date;
    private Integer total_price;
    private Integer discount_rate;
    private Float discounted_price;
    private String transaction_id;
    private String payment_status;
    private String reservation_status;
    private String created_at;
    private String updated_at;

}
