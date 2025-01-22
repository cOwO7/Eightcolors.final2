package com.springbootfinal.app.controller.room;

import com.springbootfinal.app.domain.room.Reservation;
import com.springbootfinal.app.domain.room.ReservationUserDTO;
import com.springbootfinal.app.mapper.ReservationMapper;
import com.springbootfinal.app.service.room.ReservationService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private ReservationMapper reservationMapper;


    // 예약 폼 페이지
    @GetMapping("/reservation")
    public String reservationForm(
            @RequestParam(value = "residNo", required = false) int residNo,
            @RequestParam(value = "roomNo", required = false) int roomNo,
            Model model, HttpSession httpSession) {

        int roomPrice = reservationMapper.getRoomPrice(roomNo);
        String residName = reservationService.getResidenceNameById(residNo);
        Long userNo = (Long) httpSession.getAttribute("userNo");

        ReservationUserDTO user = reservationService.getReservationUser(userNo);
        model.addAttribute("roomNo", roomNo);
        model.addAttribute("residNo", residNo);
        model.addAttribute("residName", residName);
        model.addAttribute("roomPrice", roomPrice);
        model.addAttribute("user", user);

        return "reservation"; // 뷰 이름
    }
    
	/*
	 * @ModelAttribute("roomPrice") public int getRoomPrice(@RequestParam int
	 * roomNo) { return reservationMapper.getRoomPrice(roomNo); // roomNo에 해당하는 가격
	 * 가져오기 }
	 */
    
   

    // 예약 처리
    @PostMapping("/reserve")
    public String reserveRoom(
            @ModelAttribute Reservation reservation,
            Model model) {

        log.info(String.valueOf(reservation.getUserNo()));
        // 체크인/체크아웃 날짜 유효성 검사
        if (reservation.getCheckinDate().isAfter(reservation.getCheckoutDate())) {
            model.addAttribute("errorMessage", "체크아웃 날짜는 체크인 날짜 이후여야 합니다.");
            return "test";
        }

        // 방 예약 가능 여부 확인
        boolean available = reservationService.isRoomAvailable(
                reservation.getRoomNo(),
                reservation.getCheckinDate(),
                reservation.getCheckoutDate()
        );

        if (!available) {
            model.addAttribute("errorMessage", "선택한 날짜에 방이 이미 예약되었습니다.");
            return "test";
        }

        // 예약 처리
        reservationService.reserveRoom(reservation);
        model.addAttribute("message", "예약이 완료되었습니다!");
        return "test";
    }

    @GetMapping("/test")
    public String test(){
        return "test";
    }
}