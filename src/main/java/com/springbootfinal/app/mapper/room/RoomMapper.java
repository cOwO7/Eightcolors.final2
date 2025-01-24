package com.springbootfinal.app.mapper.room;

import com.springbootfinal.app.domain.room.ReservationDTO;
import com.springbootfinal.app.domain.room.RoomDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoomMapper {
	
	  List<RoomDTO> findAvailableRoomsByHostAndDate(
			  @Param("residNo") Long residNo,@Param("currentDate") String currentDate

		    );

	  // 특정 호텔 ID와 날짜에 해당하는 예약된 방 정보를 찾는 쿼리
	  List<ReservationDTO> getReservationsByHotelIdAndDate(
		        @Param("residNo") Long residNo,
		        @Param("currentDate") String currentDate
		    );

	  List<ReservationDTO> getReservationsByResidence(@Param("residNo") Long residNo,
													  @Param("type") String type, @Param("keyword") String keyword);
}
