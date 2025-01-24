package com.springbootfinal.app.mapper.residence;

import com.springbootfinal.app.domain.residence.ResidenceDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ResidenceMapper {

    // 숙소 목록 조회
    List<ResidenceDto> getAllResidences();

    // 숙소 상세 조회
    ResidenceDto getResidenceById(@Param("residNo") Long residNo);

    // 숙소 등록
    void insertResidence(ResidenceDto residence);

    // 숙소 수정
    void updateResidence(ResidenceDto residence);

    // 숙소 삭제
    void deleteResidence(@Param("residNo") Long residNo);

    List<ResidenceDto> findByHostUserNo(Long hostUserNo);
}
