package com.springbootfinal.app.mapper;

import com.springbootfinal.app.domain.reservations.Reservations;
import com.springbootfinal.app.domain.room.Reservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ReservationMapper {

    // 방 예약 가능 여부 확인
    int checkRoomAvailability(@Param("roomNo") int roomNo,
                              @Param("checkinDate") LocalDate checkinDate,
                              @Param("checkoutDate") LocalDate checkoutDate);

    String findResidenceNameById(@Param("residNo") int residNo);
    // 방 가격 조회
    int getRoomPrice(@Param("roomNo") int roomNo);

    // 예약 정보 저장
    void insertReservation(Reservation reservation);

    public List<Reservations> getReservations(Reservations param);
    public void newReservations(Reservations param);
    public void putReservations(Reservations param);
    public void delReservations(Reservations param);

    @Select("SELECT COUNT(*) FROM reservations WHERE user_no = #{userNo}")
    int countReservationsByUserNo(@Param("userNo") String userNo);

    @Select("SELECT r.reservation_no, r.user_no, r.room_no, res.resid_name AS residName, r.checkin_date, r.checkout_date, r.total_price, r.discount_rate, r.discounted_price, r.transaction_id, r.payment_status, r.reservation_status, r.created_at, r.updated_at " +
            "FROM reservations r " +
            "JOIN residence_rooms rr ON r.room_no = rr.room_no " +
            "JOIN residence res ON rr.resid_no = res.resid_no " +
            "WHERE r.user_no = #{userNo} LIMIT 1")
    Reservations getReservationByUserNo(@Param("userNo") String userNo);



}
