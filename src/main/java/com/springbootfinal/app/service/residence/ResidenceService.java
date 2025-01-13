package com.springbootfinal.app.service.residence;

import com.springbootfinal.app.domain.residence.ResidenceDto;
import com.springbootfinal.app.mapper.residence.ResidenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResidenceService {

    private final ResidenceMapper residenceMapper;

    @Autowired
    public ResidenceService(ResidenceMapper residenceMapper) {
        this.residenceMapper = residenceMapper;
    }

    // 숙소와 관련된 사진들을 가져오는 메서드
    public ResidenceDto getResidenceWithPhotos(Long residNo) {
        return residenceMapper.getResidenceWithPhotos(residNo);
    }

    // 숙소 등록
    public void addResidence(ResidenceDto residenceDto) {
        residenceMapper.insertResidence(residenceDto);
    }

    // 숙소 ID로 조회
    public ResidenceDto getResidenceById(Long residNo) {
        return residenceMapper.getResidenceById(residNo);
    }

    // 모든 숙소 목록 조회
    public List<ResidenceDto> getAllResidences() {
        return residenceMapper.getAllResidences();
    }

    // 숙소 정보 수정
    public void updateResidence(ResidenceDto residenceDto) {
        residenceMapper.updateResidence(residenceDto);
    }

    // 숙소 삭제
    public void deleteResidence(Long residNo) {
        residenceMapper.deleteResidence(residNo);
    }
}
