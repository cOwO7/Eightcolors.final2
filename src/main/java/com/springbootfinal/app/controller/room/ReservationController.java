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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Map;

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
        Map<String, String> residenceAndRoomMap = reservationService.getResidenceAndRoomById(residNo, roomNo);

       Long userNo = (Long) httpSession.getAttribute("userNo");

        ReservationUserDTO user = reservationService.getReservationUser(userNo);
        model.addAttribute("roomNo", roomNo);
        model.addAttribute("residNo", residNo);
        model.addAttribute("residName", residenceAndRoomMap.get("residName"));
        model.addAttribute("roomName", residenceAndRoomMap.get("roomName"));
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
    @RequestMapping("/reserve")
    public String reserveRoom(
            @ModelAttribute Reservation reservation,
            Model model, HttpSession httpSession, RedirectAttributes redirectAttributes) {

        log.info(String.valueOf(reservation.getUserNo()));


        reservation.setTransactionId((String) httpSession.getAttribute("impUid"));
        reservation.setRoomNo((int)httpSession.getAttribute("roomNo"));
        reservation.setUserNo((Long)httpSession.getAttribute("userNo"));
        // HttpSession에서 checkinDate를 가져옵니다.

        Integer residNo = (Integer) httpSession.getAttribute("residNo");
        Map<String, String> residenceAndRoomMap = reservationService.getResidenceAndRoomById(residNo, reservation.getRoomNo());

        reservation.setPaymentStatus("완료");
        Object checkinDateObj = httpSession.getAttribute("checkinDate");

        try {
            // LocalDate로 변환
            LocalDate checkinDate = LocalDate.parse((CharSequence) checkinDateObj);
            log.info("체크인날짜 "+checkinDate);
            // reservation 객체에 checkoutDate 값을 설정합니다.
            reservation.setCheckinDate(checkinDate);

            log.info("체크인날짜 "+reservation.getCheckinDate());
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
            // 원하는 에러 메시지 추가 가능
            throw new IllegalArgumentException("Invalid date format or null value in session.");
        }

        Object checkoutDateObj = httpSession.getAttribute("checkoutDate");

        try {
            // LocalDate로 변환
            LocalDate checkoutDate = LocalDate.parse((CharSequence) checkoutDateObj);
            log.info("체크아웃날짜 "+checkoutDate);
            // reservation 객체에 checkoutDate 값을 설정합니다.
            reservation.setCheckoutDate(checkoutDate);
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
            // 원하는 에러 메시지 추가 가능
            throw new IllegalArgumentException("Invalid date format or null value in session.");
        }



        // 체크인/체크아웃 날짜 유효성 검사
        if (reservation.getCheckinDate().isAfter(reservation.getCheckoutDate())) {
           /* alert("체크아웃 날짜는 체크인 날짜 이후여야 합니다.");*/

       /*     Integer residNo = (Integer) httpSession.getAttribute("residNo");*/
            // alert 메시지 설정
            redirectAttributes.addFlashAttribute("alertMessage", "체크아웃 날짜는 체크인 날짜 이후여야 합니다.");

            // 리다이렉트 URL 반환
            return "redirect:/reservation?residNo=" + residNo + "&roomNo=" + reservation.getRoomNo();
        }

        // 방 예약 가능 여부 확인
        boolean available = reservationService.isRoomAvailable(
                reservation.getRoomNo(),
                reservation.getCheckinDate(),
                reservation.getCheckoutDate()
        );



        if (!available) {
           //model.addAttribute("errorMessage", "선택한 날짜에 방이 이미 예약되었습니다.");


          /*  Integer residNo = (Integer) httpSession.getAttribute("residNo");*/



            // alert 메시지 설정
            redirectAttributes.addFlashAttribute("alertMessage", "선택한 날짜에 방이 이미 예약되었습니다.");

            // 리다이렉트 URL 반환
            return "redirect:/reservation?residNo=" + residNo + "&roomNo=" + reservation.getRoomNo();
        }

        // 예약 처리
        reservationService.reserveRoom(reservation);
        Reservation reservationDetails=reservationService.getReservationByTransactionId(reservation.getTransactionId());
        model.addAttribute("reservation", reservationDetails);
        model.addAttribute("residName", residenceAndRoomMap.get("residName"));
        model.addAttribute("roomName", residenceAndRoomMap.get("roomName"));
        return "reservationSuccess";
    }

    @GetMapping("/reservationSuccess")
    public String test(){
        return "reservationSuccess";
    }
}