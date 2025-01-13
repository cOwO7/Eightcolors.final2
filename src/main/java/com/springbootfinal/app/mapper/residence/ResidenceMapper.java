package com.springbootfinal.app.mapper.residence;

import com.springbootfinal.app.domain.residence.ResidenceDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ResidenceMapper {

    // 숙소 등록
    void insertResidence(ResidenceDto residenceDto);

    // 숙소 조회 (ID로 조회)
    ResidenceDto getResidenceById(Long residNo);

    // 숙소 목록 조회
    List<ResidenceDto> getAllResidences();

    // 숙소 정보 업데이트
    void updateResidence(ResidenceDto residenceDto);

    // 숙소 삭제
    void deleteResidence(Long residNo);

    ResidenceDto getResidenceWithPhotos(Long residNo);
}
