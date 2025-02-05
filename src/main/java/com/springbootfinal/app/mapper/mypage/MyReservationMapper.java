package com.springbootfinal.app.mapper.mypage;

import com.springbootfinal.app.domain.mypage.MyReservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyReservationMapper {
    List<MyReservation> getMyReservationsByUserNo(@Param("userNo") Long userNo);
    MyReservation getReservationById(@Param("reservationNo") Long reservationNo);

}