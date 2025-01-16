package com.springbootfinal.app.service.residence;

import com.springbootfinal.app.domain.residence.PropertyPhotosDto;
import com.springbootfinal.app.mapper.residence.PropertyPhotoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;


@Service
public class PropertyPhotosService {

    private final PropertyPhotoMapper propertyPhotosMapper;

    @Autowired
    public PropertyPhotosService(PropertyPhotoMapper propertyPhotosMapper) {
        this.propertyPhotosMapper = propertyPhotosMapper;
    }

    private final String UPLOAD_DIR = "uploads/";

    public String savePhoto(MultipartFile photo) throws IOException {
        if (photo.isEmpty()) {
            throw new IllegalArgumentException("사진이 비어 있습니다.");
        }
        String fileName = UUID.randomUUID() + "_" + photo.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.copy(photo.getInputStream(), filePath);
        return fileName;
    }

    // 숙소 사진 저장 (여러 사진 저장 처리)
    public void savePhotos(List<PropertyPhotosDto> photos) {
        // 여러 사진을 한 번에 저장하는 처리
        for (PropertyPhotosDto photo : photos) {
            // 첫 번째 이미지를 썸네일로 설정
            if (photo.getThumbnailUrls() == null || photo.getThumbnailUrls().isEmpty()) {
                // 썸네일을 첫 번째 사진의 URL로 설정
                photo.setThumbnailUrls(photo.getPhotoUrl01());
            }
            propertyPhotosMapper.insertPhoto(photo);
        }
    }

    // 숙소 사진 수정
    public void updatePhoto(PropertyPhotosDto photo) {
        propertyPhotosMapper.updatePhoto(photo);
    }

    // 숙소 사진 삭제 (숙소 번호로 모든 사진 삭제)
    public void deletePhotos(Long residNo) {
        propertyPhotosMapper.deletePhoto(residNo);
    }

}

    /*// 특정 숙소의 모든 사진 조회
    public List<PropertyPhotosDto> getPhotosByResidenceId(Long residNo) {
        return propertyPhotosMapper.selectPhotosByResidenceId(residNo);
    }

    // 사진 개별 조회 (ID로 사진 조회)
    public PropertyPhotosDto getPhotoById(Long photoNo) {
        return propertyPhotosMapper.selectPhotoById(photoNo);
    }
}*/
