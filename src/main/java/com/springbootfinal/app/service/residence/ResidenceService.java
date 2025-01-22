package com.springbootfinal.app.service.residence;

import com.springbootfinal.app.domain.residence.PropertyPhotosDto;
import com.springbootfinal.app.domain.residence.ResidenceDto;
import com.springbootfinal.app.domain.residence.ResidenceRoom;
import com.springbootfinal.app.mapper.residence.PropertyPhotoMapper;
import com.springbootfinal.app.mapper.residence.ResidenceMapper;
import com.springbootfinal.app.mapper.residence.ResidenceRoomMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    /*public List<ResidenceDto> getAllResidences() {
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
    }*/

    // 숙소 상세 조회
    /*public ResidenceDto getResidenceById(Long residNo) {
        return residenceMapper.getResidenceById(residNo);
    }*/

    /*public ResidenceDto getResidenceById(Long residNo) {
        ResidenceDto residence = residenceMapper.getResidenceById(residNo);

        // 사진 데이터도 추가
        List<PropertyPhotosDto> photos = propertyPhotoMapper.getPhotosByResidNo(residNo);
        residence.setPropertyPhotos(photos);  // 사진 정보를 ResidenceDto에 설정

        return residence;
    }

    public void createResidence(ResidenceDto residence) throws IOException {
        // 숙소 등록
        residenceMapper.insertResidence(residence);
        Long residNo = residence.getResidNo(); // 등록된 residNo를 가져옴

        log.info("residNo : {}", residNo);
    }



    // 숙소 수정
    public void updateResidence(ResidenceDto residence, Long residNo,
                                List<MultipartFile> photos) throws IOException {
        // 1. 숙소 정보 업데이트
        residenceMapper.updateResidence(residence);

        // 2. 새로운 사진 추가
        if (photos != null && !photos.isEmpty()) {
            propertyPhotosService.updatePhoto(residNo, photos);
        }
    }


    // 숙소 삭제
    public void deleteResidence(Long residNo) {
        // 방 및 사진도 함께 삭제
        residenceRoomMapper.deleteRoom(residNo);
        propertyPhotoMapper.deletePhoto(residNo);
        residenceMapper.deleteResidence(residNo);
    }*/

    // 방 목록 조회
    public List<ResidenceRoom> getRoomsByResidenceId(Long residNo) {
        return residenceRoomMapper.getRoomsByResidenceId(residNo);
    }

    // 피티 최종코드 2
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
                residence.setNewPhotoUrls(photoUrls);

            if (!photoUrls.isEmpty()) {
                residence.setThumbnailUrls(photoUrls.get(0));
            }
        });
        return residences;
    }

    public ResidenceDto getResidenceById(Long residNo) {
        ResidenceDto residence = residenceMapper.getResidenceById(residNo);
        List<PropertyPhotosDto> photos = propertyPhotoMapper.getPhotosByResidNo(residNo);
        residence.setPropertyPhotos(photos);
        List<ResidenceRoom> rooms = residenceRoomMapper.getRoomsByResidenceId(residNo);
        residence.setRooms(rooms);
        return residence;
    }

    public void createResidence(ResidenceDto residence,
                                ResidenceRoom room) throws IOException {
        residenceMapper.insertResidence(residence);
        residenceRoomMapper.insertRoom(room);
        Long residNo = residence.getResidNo();
        log.info("residNo : {}", residNo);
    }

    public void updateResidence(ResidenceDto residence, Long residNo, List<MultipartFile> photos) throws IOException {
        residenceMapper.updateResidence(residence);
        if (photos != null && !photos.isEmpty()) {
            propertyPhotosService.updatePhoto(residNo, photos);
        }
    }

    public void deleteResidence(Long residNo) {
        residenceRoomMapper.deleteRoom(residNo);
        propertyPhotoMapper.deletePhoto(residNo);
        residenceMapper.deleteResidence(residNo);
    }

}
