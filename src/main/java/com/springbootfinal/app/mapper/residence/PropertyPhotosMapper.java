package com.springbootfinal.app.mapper.residence;

import com.springbootfinal.app.domain.residence.PropertyPhotosDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PropertyPhotosMapper {

    // 숙소 사진 등록
    void insertPropertyPhoto(PropertyPhotosDto propertyPhotosDto);

    // 숙소 번호로 사진 조회
    List<PropertyPhotosDto> getPhotosByResidenceId(Long residNo);

    // 숙소 사진 수정
    void updatePropertyPhoto(PropertyPhotosDto propertyPhotosDto);

    // 숙소 사진 삭제
    void deletePropertyPhoto(Long photoNo);

    // 숙소 번호로 모든 사진 삭제 (숙소 삭제 시)
    void deletePhotosByResidenceId(Long residNo);
}