package com.springbootfinal.app.mapper.residence;

import com.springbootfinal.app.domain.residence.PropertyPhotosDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PropertyPhotoMapper {

    // 특정 숙소의 사진 목록 조회
    List<PropertyPhotosDto> getPhotosByResidNo(@Param("residNo") Long residNo);

    // 특정 숙소의 사진 파일 URL 조회 (파일 삭제 시 사용)
    List<String> getPhotoUrlsByPhotoNo(@Param("photoNo") Long photoNo);

    // 사진 등록
    void insertPhoto(PropertyPhotosDto photo);

    // 사진 삭제 (숙소 번호 기준)
    void deletePhoto(@Param("residNo") Long residNo);

    // 사진 수정
    void updatePhoto(PropertyPhotosDto photo);

    // 방 사진 등록
    String insertRoom(@Param("roomNo") Long roomNo);

    // 방 사진 수정
    void updateRoom(PropertyPhotosDto photo);

    // 방 사진 삭제
    void deleteRoom(@Param("roomNo") Long roomNo);
}