package com.springbootfinal.app.service.residence;

import com.springbootfinal.app.domain.residence.PropertyPhotosDto;
import com.springbootfinal.app.mapper.residence.PropertyPhotosMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyPhotosService {

    private final PropertyPhotosMapper propertyPhotosMapper;

    @Autowired
    public PropertyPhotosService(PropertyPhotosMapper propertyPhotosMapper) {
        this.propertyPhotosMapper = propertyPhotosMapper;
    }

    // 숙소 사진 저장
    public void savePhotos(PropertyPhotosDto photo) {
        propertyPhotosMapper.insertPhoto(photo);
    }

    // 숙소 사진 수정
    public void updatePhoto(PropertyPhotosDto photo) {
        propertyPhotosMapper.updatePhoto(photo);
    }

    // 숙소 사진 삭제
    public void deletePhotos(Long residNo) {
        propertyPhotosMapper.deletePhotosByResidenceId(residNo);
    }
}