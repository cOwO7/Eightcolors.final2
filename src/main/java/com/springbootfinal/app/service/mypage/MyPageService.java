package com.springbootfinal.app.service.mypage;

import com.springbootfinal.app.domain.mypage.MyReservation;
import com.springbootfinal.app.mapper.mypage.MyReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyPageService {

    @Autowired
    private MyReservationMapper myReservationMapper;

    public List<MyReservation> getMyReservationsByUserNo(Long userNo) {
        return myReservationMapper.getMyReservationsByUserNo(userNo);
    }

    public MyReservation getReservationById(Long reservationNo) {
        return myReservationMapper.getReservationById(reservationNo);
    }
}
