package com.springbootfinal.app.controller;


import com.springbootfinal.app.domain.ReservationDTO;
import com.springbootfinal.app.domain.RoomDTO;
import com.springbootfinal.app.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class RoomController {

   
	   @Autowired
	    private RoomService roomService;

	   /* // 예약 가능한 방 조회 (뷰 반환)
	    @GetMapping("/available")
	    public String getAvailableRooms(@RequestParam("hotelId") int hotelId, @RequestParam("date") String date, Model model) {
	        List<RoomDTO> rooms = roomService.getAvailableRooms(hotelId, date);
	        model.addAttribute("rooms", rooms);
	        return "available"; // availableRooms.html 뷰 반환
	    }

	    // 예약된 방 조회 (오늘 날짜 기준)
	    @GetMapping("/booked")
	    public String getBookedRooms(@RequestParam("hotelId") int hotelId, Model model) {
	        List<RoomDTO> bookedRooms = roomService.getBookedRooms(hotelId);
	        model.addAttribute("bookedRooms", bookedRooms);
	        return "bookedRooms"; // 예약된 방을 보여줄 뷰
	    }*/
	    
	 // 예약된 방 조회 (오늘 날짜 기준)
	    @GetMapping("/availableAndBookedRooms")
	    public String getavailableAndBookedRooms(@RequestParam("hotelId") int hotelId, Model model) {
	        List<ReservationDTO> bookedRooms = roomService.getReservations(hotelId);
	        List<RoomDTO> rooms = roomService.getAvailableRoomsByHostAndDate(hotelId);
	        model.addAttribute("rooms", rooms);
	        model.addAttribute("bookedRooms", bookedRooms);
	        return "availableAndBookedRooms"; // 예약된 방을 보여줄 뷰
	    }
	    
	    @GetMapping("/room")
	    public String rooms(Model model) {
	    	return "stayBookingLookup";
	    }
	    
}
