package com.springbootfinal.app.service;


import com.springbootfinal.app.domain.ReservationDTO;
import com.springbootfinal.app.domain.RoomDTO;
import com.springbootfinal.app.mapper.RoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService {

	    @Autowired
	    private RoomMapper roomMapper;

	   /* // 예약 가능한 방 조회
	 public List<RoomDTO> getAvailableRooms(int hotelId, String date) {
	        return roomMapper.getAvailableRooms(hotelId, date);
	    }

	    // 특정 호텔에 대해 오늘 날짜 기준 예약된 방을 조회
	 public List<RoomDTO> getBookedRooms(int hotelId) {
	        return roomMapper.getBookedRooms(hotelId, LocalDate.now().toString());
	    }
	 
	 public List<ReservationDTO> getCurrentStayReservations(int hotelId) {
	        String currentDate = LocalDate.now().toString(); // 오늘 날짜를 가져옴
	        return roomMapper.findCurrentStayReservations(currentDate, hotelId);
	    }
	 
	    public List<ReservationDTO> getReservationsForHotel( int hotelId) {
	    	String currentDate = LocalDate.now().toString(); // 오늘 날짜를 가져옴
	        return roomMapper.getReservationsForHotel(currentDate, hotelId);
	    }*/
	    
	    public List<RoomDTO> getAvailableRoomsByHostAndDate(int hostUserNo) {
	    	String currentDate = LocalDate.now().toString(); // 오늘 날짜를 가져옴
	        return roomMapper.findAvailableRoomsByHostAndDate(hostUserNo, currentDate);
	    }
	    
	    public List<ReservationDTO> getReservations(int hotelId) {
	    	String currentDate = LocalDate.now().toString(); // 오늘 날짜를 가져옴
	        return roomMapper.getReservationsByHotelIdAndDate(hotelId, currentDate);
	    }

	

}
