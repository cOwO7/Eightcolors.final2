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

    // 숙소 사진 등록
    public void addPropertyPhoto(PropertyPhotosDto propertyPhotosDto) {
        propertyPhotosMapper.insertPropertyPhoto(propertyPhotosDto);
    }

    // 숙소 번호로 사진 조회
    public List<PropertyPhotosDto> getPhotosByResidenceId(Long residNo) {
        return propertyPhotosMapper.getPhotosByResidenceId(residNo);
    }

    // 숙소 사진 수정
    public void updatePropertyPhoto(PropertyPhotosDto propertyPhotosDto) {
        propertyPhotosMapper.updatePropertyPhoto(propertyPhotosDto);
    }

    // 숙소 사진 삭제
    public void deletePropertyPhoto(Long photoNo) {
        propertyPhotosMapper.deletePropertyPhoto(photoNo);
    }

    // 숙소 번호로 모든 사진 삭제 (숙소 삭제 시)
    public void deletePhotosByResidenceId(Long residNo) {
        propertyPhotosMapper.deletePhotosByResidenceId(residNo);
    }
}