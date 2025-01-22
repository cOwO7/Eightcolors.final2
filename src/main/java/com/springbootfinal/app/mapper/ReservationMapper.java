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
}
