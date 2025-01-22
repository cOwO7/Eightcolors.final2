package com.springbootfinal.app.service.room;

import com.springbootfinal.app.domain.room.Reservation;
import com.springbootfinal.app.domain.room.ReservationUserDTO;
import com.springbootfinal.app.mapper.ReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class ReservationService {

    @Autowired
    private ReservationMapper reservationMapper;

    // 방 예약 가능 여부 체크
    public boolean isRoomAvailable(int roomNo, LocalDate checkinDate, LocalDate checkoutDate) {
        return reservationMapper.checkRoomAvailability(roomNo, checkinDate, checkoutDate) == 0;
    }
    public String getResidenceNameById(int residNo) {
        return  reservationMapper.findResidenceNameById(residNo);
    }

    // 예약 처리
    public void reserveRoom(Reservation reservation) {
        // 할인율 계산
        long daysUntilCheckin = ChronoUnit.DAYS.between(LocalDate.now(), reservation.getCheckinDate())+1;
        int discountRate = 0;

        if (daysUntilCheckin == 3) {
            discountRate = 10; // 3일 전 예약: 10% 할인
        } else if (daysUntilCheckin == 2) {
            discountRate = 20; // 2일 전 예약: 20% 할인
        } else if (daysUntilCheckin == 1) {
            discountRate = 30; // 1일 전 예약: 30% 할인
        }

        // 할인된 가격 계산
        int originalPrice = reservationMapper.getRoomPrice(reservation.getRoomNo())*(int)ChronoUnit.DAYS.between(reservation.getCheckinDate(), reservation.getCheckoutDate());
        int discountedPrice = originalPrice - (originalPrice * discountRate / 100);

        // 할인율과 최종 가격 설정
        reservation.setTotalprice(originalPrice);
        reservation.setDiscountRate(discountRate);
        reservation.setDiscountedPrice(discountedPrice);
        reservation.setTransactionId(UUID.randomUUID().toString());
        // 예약 정보 저장
        reservationMapper.insertReservation(reservation);
    }
    
    //예약하는 사용자 정보 조회
    public ReservationUserDTO getReservationUser(Long userNo) {
        return reservationMapper.getReservationUserByUserNo(userNo);
    }
}
