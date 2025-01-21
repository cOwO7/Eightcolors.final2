package com.springbootfinal.app.mapper.residence;

import com.springbootfinal.app.domain.residence.PropertyPhotosDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PropertyPhotoMapper {

    // 사진 목록 조회
    List<PropertyPhotosDto> getPhotosByResidenceId(Long residNo);

    // 사진 등록
    void insertPhoto(PropertyPhotosDto photo);

    // 사진 삭제
    void deletePhoto(Long residNo);
    List<String> getPhotoFilesByResidNo(Long residNo); // No에 해당하는 모든 사진 삭제

    // 사진 수정
    void updatePhoto(Long residNo, Long photo);
    List<PropertyPhotosDto> getPhotosByResidNo(Long residNo);
}