package com.springbootfinal.app.service.residence;

import com.springbootfinal.app.domain.residence.ResidenceDto;
import com.springbootfinal.app.mapper.residence.PropertyPhotoMapper;
import com.springbootfinal.app.mapper.residence.ResidenceMapper;
import com.springbootfinal.app.mapper.residence.ResidenceRoomMapper;
import com.springbootfinal.app.service.weather.AllWeatherService;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ResidenceService {

    private final ResidenceMapper residenceMapper;
    private final ResidenceRoomMapper residenceRoomMapper;
    private final PropertyPhotoMapper propertyPhotoMapper;

    @Autowired
    public ResidenceService(ResidenceMapper residenceMapper,
                            ResidenceRoomMapper residenceRoomMapper,
                            PropertyPhotoMapper propertyPhotoMapper) {
        this.residenceMapper = residenceMapper;
        this.residenceRoomMapper = residenceRoomMapper;
        this.propertyPhotoMapper = propertyPhotoMapper;
    }

    // 숙소 목록 조회
    public List<ResidenceDto> getAllResidences() {
        List<ResidenceDto> residences = residenceMapper.getAllResidences();

        residences.forEach(residence -> {
            List<String> photoUrls = new ArrayList<>();
            if (residence.getPhotoUrl01() != null) photoUrls.add(residence.getPhotoUrl01());
            if (residence.getPhotoUrl02() != null) photoUrls.add(residence.getPhotoUrl02());
            if (residence.getPhotoUrl03() != null) photoUrls.add(residence.getPhotoUrl03());
            if (residence.getPhotoUrl04() != null) photoUrls.add(residence.getPhotoUrl04());
            if (residence.getPhotoUrl05() != null) photoUrls.add(residence.getPhotoUrl05());
            if (residence.getPhotoUrl06() != null) photoUrls.add(residence.getPhotoUrl06());
            if (residence.getPhotoUrl07() != null) photoUrls.add(residence.getPhotoUrl07());
            if (residence.getPhotoUrl08() != null) photoUrls.add(residence.getPhotoUrl08());
            if (residence.getPhotoUrl09() != null) photoUrls.add(residence.getPhotoUrl09());
            if (residence.getPhotoUrl10() != null) photoUrls.add(residence.getPhotoUrl10());

            residence.setPhotoUrls(photoUrls);

            // 첫 번째 URL을 썸네일로 설정
            if (!photoUrls.isEmpty()) {
                residence.setThumbnailUrl(photoUrls.get(0));
            }
        });

        return residences;
    }

    // 숙소 상세 조회
    public ResidenceDto getResidenceById(Long residNo) {
        return residenceMapper.getResidenceById(residNo);
    }

    // 숙소 등록
    public void createResidence(ResidenceDto residence) {
        log.info("In Service, ResidenceDto: {}", residence); // 서비스에서 DTO 확인
        residenceMapper.insertResidence(residence); // MyBatis 매퍼 호출
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
