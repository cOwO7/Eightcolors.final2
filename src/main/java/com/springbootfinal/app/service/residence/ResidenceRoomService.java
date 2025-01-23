package com.springbootfinal.app.service.residence;

import com.springbootfinal.app.domain.residence.ResidenceRoom;
import com.springbootfinal.app.mapper.residence.ResidenceRoomMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class ResidenceRoomService {


    private final ResidenceRoomMapper residenceRoomMapper;

    @Autowired
    public ResidenceRoomService(ResidenceRoomMapper residenceRoomMapper) {
        this.residenceRoomMapper = residenceRoomMapper;
    }
    // 방 등록
    public void createResidenceRoom (ResidenceRoom residenceRoom) {

        residenceRoomMapper.insertRoom(residenceRoom);
        /*Long residNo = residence.getResidNo();
        log.info("residNo : {}", residNo);*/
    }
    // 방 수정
    public void updateRoom (ResidenceRoom residenceRoom, Long residNo) {
        residenceRoom.setResidNo(residNo);
        residenceRoomMapper.updateRoom(residenceRoom);
    }
    // 방 삭제
    public void deleteRoom (Long residNo) {
        residenceRoomMapper.deleteRoom(residNo);
    }

}
