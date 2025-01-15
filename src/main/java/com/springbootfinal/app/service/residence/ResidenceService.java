package com.springbootfinal.app.service.residence;

import com.springbootfinal.app.domain.residence.ResidenceDto;
import com.springbootfinal.app.mapper.residence.PropertyPhotoMapper;
import com.springbootfinal.app.mapper.residence.ResidenceMapper;
import com.springbootfinal.app.mapper.residence.ResidenceRoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResidenceService {

    @Autowired
    private ResidenceMapper residenceMapper;

    @Autowired
    private ResidenceRoomMapper residenceRoomMapper;

    @Autowired
    private PropertyPhotoMapper propertyPhotoMapper;

    // 숙소 목록 조회
    public List<ResidenceDto> getAllResidences() {
        return residenceMapper.getAllResidences();
    }

    // 숙소 상세 조회
    public ResidenceDto getResidenceById(Long residNo) {
        return residenceMapper.getResidenceById(residNo);
    }

    // 숙소 등록
    public void createResidence(ResidenceDto residence) {
        residenceMapper.insertResidence(residence);
    }

    // 숙소 수정
    public void updateResidence(ResidenceDto residence) {
        residenceMapper.updateResidence(residence);
    }

    // 숙소 삭제
    public void deleteResidence(Long residNo) {
        // 방 및 사진도 함께 삭제
        residenceRoomMapper.deleteRoom(residNo);
        propertyPhotoMapper.deletePhoto(residNo);
        residenceMapper.deleteResidence(residNo);
    }

}
