package com.springbootfinal.app.mapper;

import com.springbootfinal.app.domain.reservations.Reservations;
import com.springbootfinal.app.domain.room.Reservation;
import com.springbootfinal.app.domain.room.ReservationUserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface ReservationMapper {

    // 방 예약 가능 여부 확인
    int checkRoomAvailability(@Param("roomNo") int roomNo,
                              @Param("checkinDate") LocalDate checkinDate,
                              @Param("checkoutDate") LocalDate checkoutDate);

    Map<String, String> findResidenceAndRoomById(@Param("residNo") int residNo, @Param("roomNo") int roomNo);
    // 방 가격 조회
    int getRoomPrice(@Param("roomNo") int roomNo);

    // 예약 정보 저장
    void insertReservation(Reservation reservation);
    
    //예약하는 사용자 정보 조회
    ReservationUserDTO getReservationUserByUserNo(@Param("userNo") Long userNo);

    public List<Reservations> getReservations(Reservations param);
    public void newReservations(Reservations param);
    public void putReservations(Reservations param);
    public void delReservations(Reservations param);

    Reservation selectReservationByTransactionId(@Param("transactionId") String transactionId);

}
