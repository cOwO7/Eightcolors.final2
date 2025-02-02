package com.springbootfinal.app.mapper;

import com.springbootfinal.app.domain.reservations.Reservations;
import com.springbootfinal.app.domain.room.Reservation;
import com.springbootfinal.app.domain.room.ReservationUserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Mapper
public interface ReservationMapper {

    // 방 예약 가능 여부 확인
    int checkRoomAvailability(@Param("roomNo") Long roomNo,
                              @Param("checkinDate") LocalDate checkinDate,
                              @Param("checkoutDate") LocalDate checkoutDate);

    Map<String, String> findResidenceAndRoomById(@Param("residNo") Long residNo, @Param("roomNo") Long roomNo);
    // 방 가격 조회
    int getRoomPrice(@Param("roomNo") Long roomNo);

    // 예약 정보 저장
    void insertReservation(Reservation reservation);

    //예약하는 사용자 정보 조회
    ReservationUserDTO getReservationUserByUserNo(@Param("userNo") Long userNo);

    List<Reservations> getReservations(Reservations param);
    void newReservations(Reservations param);
    void putReservations(Reservations param);
    void delReservations(Reservations param);


    Reservation selectReservationByTransactionId(@Param("transactionId") String transactionId);

    @Select("SELECT COUNT(*) FROM reservations WHERE user_no = #{userNo}")
    int countReservationsByUserNo(@Param("userNo") String userNo);

    /*
     * @Select("SELECT r.reservation_no, r.user_no, r.room_no, res.resid_name AS residName, r.checkin_date, r.checkout_date, r.total_price, r.discount_rate, r.discounted_price, r.transaction_id, r.payment_status, r.reservation_status, r.created_at, r.updated_at "
     * + "FROM reservations r " +
     * "JOIN residence_rooms rr ON r.room_no = rr.room_no " +
     * "JOIN residence res ON rr.resid_no = res.resid_no " +
     * "WHERE r.user_no = #{userNo} LIMIT 1") Reservations
     * getReservationByUserNo(@Param("userNo") String userNo);
     */

   /*@Select("SELECT r.reservation_no, r.user_no, r.room_no, res.resid_name AS residName, r.checkin_date, r.checkout_date, r.total_price, r.discount_rate, r.discounted_price, r.transaction_id, r.payment_status, r.reservation_status, r.created_at, r.updated_at " +
            "FROM reservations r " +
            "JOIN residence_rooms rr ON r.room_no = rr.room_no " +
            "JOIN residence res ON rr.resid_no = res.resid_no " +
            "WHERE r.user_no = #{userNo} LIMIT 1")
    Reservations getReservationByUserNo(@Param("userNo") Long userNo);*/


    Reservations getReservationByUserNo(@Param("userNo") Long userNo);



}
