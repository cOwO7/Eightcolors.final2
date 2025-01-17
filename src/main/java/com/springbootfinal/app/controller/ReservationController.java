package com.springbootfinal.app.controller;

import com.springbootfinal.app.domain.Reservation;
import com.springbootfinal.app.mapper.ReservationMapper;
import com.springbootfinal.app.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Controller
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    // 예약 폼 페이지
    @GetMapping("/reservation/form")
    public String reservationForm(
            @RequestParam int roomNo,
            @RequestParam int residNo,
            Model model) {
        String residName = reservationService.getResidenceNameById(residNo);
        model.addAttribute("roomNo", roomNo);
        model.addAttribute("residNo", residNo);
        model.addAttribute("residName", residName);
        return "reservation"; // 뷰 이름
    }

    // 예약 처리
    @PostMapping("/reserve")
    public String reserveRoom(
            @ModelAttribute Reservation reservation,
            Model model) {
        // 체크인/체크아웃 날짜 유효성 검사
        if (reservation.getCheckinDate().isAfter(reservation.getCheckoutDate())) {
            model.addAttribute("errorMessage", "체크아웃 날짜는 체크인 날짜 이후여야 합니다.");
            return "reservation";
        }

        // 방 예약 가능 여부 확인
        boolean available = reservationService.isRoomAvailable(
                reservation.getRoomNo(),
                reservation.getCheckinDate(),
                reservation.getCheckoutDate()
        );

        if (!available) {
            model.addAttribute("errorMessage", "선택한 날짜에 방이 이미 예약되었습니다.");
            return "reservation";
        }

        // 예약 처리
        reservationService.reserveRoom(reservation);
        model.addAttribute("message", "예약이 완료되었습니다!");
        return "reservation";
    }
}