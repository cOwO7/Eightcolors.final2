package com.springbootfinal.app.service.residence;

import com.springbootfinal.app.domain.residence.PropertyPhotosDto;
import com.springbootfinal.app.mapper.residence.PropertyPhotoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PropertyPhotosService {

    private final PropertyPhotoMapper propertyPhotosMapper;

    @Autowired
    public PropertyPhotosService(PropertyPhotoMapper propertyPhotosMapper) {
        this.propertyPhotosMapper = propertyPhotosMapper;
    }

    // 사진 등록 시 local에 저장되는 경로
    @Value("${UPLOAD_DIR}")
    private String UPLOAD_DIR;

    // 사진 유효성 검사
    public void validatePhoto(MultipartFile photo) {
        long maxSize = 5 * 1024 * 1024; // 최대 5MB
        String[] allowedExtensions = {"jpg", "jpeg", "png", "gif"};

        if (photo.getSize() > maxSize) {
            throw new IllegalArgumentException("파일 크기가 너무 큽니다.");
        }

        String originalFileName = photo.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();

        if (!Arrays.asList(allowedExtensions).contains(fileExtension)) {
            throw new IllegalArgumentException("지원되지 않는 파일 형식입니다.");
        }
    }

    // 파일 저장 메서드 (PropertyPhotosDto 객체 추가)
    public String savePhoto(MultipartFile photo, String fileName, PropertyPhotosDto photoDto) throws IOException {
        if (photo.isEmpty()) {
            throw new IllegalArgumentException("사진이 비어 있습니다.");
        }

        // 유효성 검사
        validatePhoto(photo);

        // 파일 확장자 추출
        String originalFileName = photo.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        // 확장자 검증
        if (!isValidExtension(fileExtension)) {
            throw new IllegalArgumentException("허용되지 않은 파일 확장자입니다.");
        }

        // 만약 fileName에 확장자가 포함되어 있으면 제거
        if (fileName.lastIndexOf(".") > -1) {
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        }

        // 고유 파일명 생성 (fileName + 확장자)
        String fullFileName = fileName + fileExtension;

        // 경로 설정
        Path filePath = Paths.get(UPLOAD_DIR + fullFileName);

        // 디렉토리가 존재하지 않으면 생성
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 파일 저장
        Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 사진 DTO에 저장된 파일명 설정
        photoDto.setThumbnailUrls(fullFileName);

        // DB에 저장
        propertyPhotosMapper.insertPhoto(photoDto);

        return fullFileName;  // 저장된 파일명 반환
    }
    /*public String savePhoto(MultipartFile photo, String fileName, PropertyPhotosDto photoDto) throws IOException {
        if (photo.isEmpty()) {
            throw new IllegalArgumentException("사진이 비어 있습니다.");
        }

        // 유효성 검사
        validatePhoto(photo);

        // 파일 확장자 추출 (기존 방식 유지)
        String originalFileName = photo.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        // 확장자 검증
        if (!isValidExtension(fileExtension)) {
            throw new IllegalArgumentException("허용되지 않은 파일 확장자입니다.");
        }

        // 만약 fileName에 확장자가 포함되어 있으면 제거
        if (fileName.lastIndexOf(".") > -1) {
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        }

        // 고유 파일명 생성 (fileName + 확장자)
        String fullFileName = fileName + fileExtension;

        // 경로 설정
        Path filePath = Paths.get(UPLOAD_DIR + fullFileName);

        // 디렉토리가 존재하지 않으면 생성
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 파일 저장
        Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 파일 경로를 PropertyPhotosDto에 설정
        photoDto.setPhotoUrl01("/uploads/" + fullFileName);

        return fullFileName;  // 저장된 파일명 반환
    }*/


    // 파일 확장자 검증
    private boolean isValidExtension(String fileExtension) {
        List<String> validExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".bmp");
        return validExtensions.contains(fileExtension.toLowerCase());
    }

    // 숙소 사진 저장 (여러 사진 저장 처리)
    public void savePhotos(List<PropertyPhotosDto> photos) {
        for (PropertyPhotosDto photo : photos) {
            if (photo.getThumbnailUrls() == null || photo.getThumbnailUrls().isEmpty()) {
                photo.setThumbnailUrls(photo.getPhotoUrl01());
            }
            log.info("Inserting photo: {}", photo);
            propertyPhotosMapper.insertPhoto(photo);
        }
    }

    // 숙소 사진 삭제 (파일과 DB에서 삭제)
    public void deletePhotos(Long residNo) {
        List<String> photoFiles = propertyPhotosMapper.getPhotoFilesByResidNo(residNo);
        for (String fileName : photoFiles) {
            try {
                deletePhotoFile(fileName);
            } catch (IOException e) {
                // 예외 처리 (파일 삭제 실패 시)
            }
        }
        propertyPhotosMapper.deletePhoto(residNo);  // DB에서 사진 삭제
    }

    private void deletePhotoFile(String fileName) throws IOException {
        String filePath = UPLOAD_DIR + fileName;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();  // 실제 파일 삭제
        }
    }
}
/*
public void deletePhotos(Long residNo) {
    propertyPhotosMapper.deletePhoto(residNo);
}*/
