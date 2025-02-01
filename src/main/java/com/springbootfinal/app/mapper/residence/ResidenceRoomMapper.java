package com.springbootfinal.app.mapper.residence;

import com.springbootfinal.app.domain.residence.ResidenceRoom;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ResidenceRoomMapper {

    // 방 목록 조회
    List<ResidenceRoom> getRoomsByResidenceId(Long residNo);
    String getRoomImageByRoomNo(Long roomNo);

    // 방 등록
    void insertRoom(ResidenceRoom room);

    // 방 수정
    void updateRoom(ResidenceRoom room);

    // 방 삭제
    void deleteRoom(Long roomNo);

}