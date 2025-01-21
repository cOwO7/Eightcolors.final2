package com.springbootfinal.app.service.residence;

import com.springbootfinal.app.domain.residence.PropertyPhotosDto;
import com.springbootfinal.app.domain.residence.ResidenceDto;
import com.springbootfinal.app.mapper.residence.PropertyPhotoMapper;
import com.springbootfinal.app.mapper.residence.ResidenceMapper;
import com.springbootfinal.app.mapper.residence.ResidenceRoomMapper;
import com.springbootfinal.app.service.weather.AllWeatherService;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ResidenceService {

    private final ResidenceMapper residenceMapper;
    private final ResidenceRoomMapper residenceRoomMapper;
    private final PropertyPhotoMapper propertyPhotoMapper;
    private final PropertyPhotosService propertyPhotosService;

    @Autowired
    public ResidenceService(ResidenceMapper residenceMapper,
                            ResidenceRoomMapper residenceRoomMapper,
                            PropertyPhotoMapper propertyPhotoMapper, PropertyPhotosService propertyPhotosService) {
        this.residenceMapper = residenceMapper;
        this.residenceRoomMapper = residenceRoomMapper;
        this.propertyPhotoMapper = propertyPhotoMapper;
        this.propertyPhotosService = propertyPhotosService;
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
    /*public ResidenceDto getResidenceById(Long residNo) {
        return residenceMapper.getResidenceById(residNo);
    }*/
    public ResidenceDto getResidenceById(Long residNo) {
        ResidenceDto residence = residenceMapper.getResidenceById(residNo);

        // 사진 데이터도 추가
        List<PropertyPhotosDto> photos = propertyPhotoMapper.getPhotosByResidenceId(residNo);
        residence.setPropertyPhotos(photos);  // 사진 정보를 ResidenceDto에 설정

        return residence;
    }

    public void createResidence(ResidenceDto residence, MultipartFile[] photoFiles) throws IOException {
        // 사진 파일이 null이거나 비어 있으면 예외 처리
        if (photoFiles == null || photoFiles.length == 0) {
            throw new IllegalArgumentException("사진 파일이 없습니다.");
        }

        // 숙소 등록
        residenceMapper.insertResidence(residence);
        Long residNo = residence.getResidNo(); // 등록된 residNo를 가져옴

        log.info("residNo : {}", residNo);

        // 사진 처리
        List<PropertyPhotosDto> photos = new ArrayList<>();
        for (MultipartFile file : photoFiles) {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            String savedFileName = propertyPhotosService.savePhoto(file, fileName, residNo);

            PropertyPhotosDto photoDto = new PropertyPhotosDto();
            photoDto.setResidNo(residNo);  // residNo를 설정
            photoDto.setThumbnailUrls(savedFileName);

            photos.add(photoDto);
        }
        // 사진 DB 저장
        propertyPhotosService.savePhotos(photos);
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
