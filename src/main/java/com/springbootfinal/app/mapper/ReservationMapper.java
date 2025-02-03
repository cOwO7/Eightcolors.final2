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

    // 숙소 및 방 정보 조회
    Map<String, String> findResidenceAndRoomById(@Param("residNo") Long residNo, @Param("roomNo") Long roomNo);

    // 방 가격 조회
    int getRoomPrice(@Param("roomNo") Long roomNo);

    // 예약 정보 저장
    void insertReservation(Reservation reservation);

    //예약하는 사용자 정보 조회
    ReservationUserDTO getReservationUserByUserNo(@Param("userNo") Long userNo);

    // 예약 정보 조회
    List<Reservations> getReservations(Reservations param);
    void newReservations(Reservations param);
    void putReservations(Reservations param);
    void delReservations(Reservations param);

    // 예약 정보 조회
    Reservation selectReservationByTransactionId(@Param("transactionId") String transactionId);

    // 예약 정보 조회
    @Select("SELECT COUNT(*) FROM reservations WHERE user_no = #{userNo}")
    int countReservationsByUserNo(@Param("userNo") String userNo);

    // 예약 정보 조회
    Reservations getReservationByUserNo(@Param("userNo") Long userNo);



}
