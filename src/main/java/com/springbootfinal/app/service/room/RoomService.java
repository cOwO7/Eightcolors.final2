package com.springbootfinal.app.service.room;


import com.springbootfinal.app.domain.room.ReservationDTO;
import com.springbootfinal.app.domain.room.RoomDTO;
import com.springbootfinal.app.mapper.room.RoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	    /*오늘기준 예약가능한방*/
	    public List<RoomDTO> getAvailableRoomsByHostAndDate(int hostUserNo) {
	    	String currentDate = LocalDate.now().toString(); // 오늘 날짜를 가져옴
	        return roomMapper.findAvailableRoomsByHostAndDate(hostUserNo, currentDate);
	    }
		/*오늘기준 투숙중인 내역*/
	    public List<ReservationDTO> getReservations(int hotelId) {
	    	String currentDate = LocalDate.now().toString(); // 오늘 날짜를 가져옴
	        return roomMapper.getReservationsByHotelIdAndDate(hotelId, currentDate);
	    }

		public Map<String, Object> getReservationsByResidence(int hotelId, String type, String keyword) {
			boolean searchOption = (type.equals("null")
					|| keyword.equals("null")) ? false : true;
			Map<String, Object> modelMap = new HashMap<String, Object>();

			modelMap.put("searchOption",searchOption);
			List<ReservationDTO> BookingList=roomMapper.getReservationsByResidence(hotelId,type,keyword);
			if(searchOption){
				modelMap.put("type", type);
				modelMap.put("keyword", keyword);
			}

			modelMap.put("bookingList",BookingList);
			return modelMap;
		}

	

}
