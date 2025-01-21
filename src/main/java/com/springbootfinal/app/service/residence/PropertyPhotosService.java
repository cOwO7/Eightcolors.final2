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
import java.net.URLEncoder;
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

    // 파일 저장 경로 생성
    private Path getFilePath(String fileName) {
        return Paths.get(UPLOAD_DIR).resolve(fileName);  // 안전한 경로 생성
    }

    // 사진 등록 시 유효성 검사 및 저장
    public String savePhoto(MultipartFile photo, String fileName, Long residNo) throws IOException {
        if (photo.isEmpty()) {
            throw new IllegalArgumentException("사진이 비어 있습니다.");
        }

        // 유효성 검사
        validatePhoto(photo);

        // 파일 확장자 추출
        String originalFileName = photo.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();

        if (!isValidExtension(fileExtension)) {
            throw new IllegalArgumentException("허용되지 않은 파일 확장자입니다.");
        }

        // 확장자 중복 방지 및 파일명 처리
        if (fileName.toLowerCase().endsWith(fileExtension)) {
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        }

        String fullFileName = fileName + fileExtension;  // 확장자 추가

        // 경로 설정
        Path filePath = getFilePath(fullFileName);

        // 디렉토리 존재 여부 확인 및 생성
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 파일 저장
        Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        log.info("Saved photo: {}", fullFileName);
        return fullFileName;
    }

    // 사진 등록 (여러 사진 저장 처리)
    /* // 파일 개수 제한 (최대 10개)
        if (photos.size() > 10) {
            log.error("Too many files received: {}", photos.size());
            throw new IllegalArgumentException("최대 10개의 파일만 업로드 가능합니다.");
        }

        for (PropertyPhotosDto photo : photos) {
            if (photo.getResidNo() == null) {
                log.error("resid_no is null for photo: {}", photo);
                throw new IllegalArgumentException("resid_no must not be null");
            }

            // 썸네일 설정: 첫 번째 사진만 썸네일로 설정하도록 수정
            if (photo.getThumbnailUrls() == null || photo.getThumbnailUrls().isEmpty()) {
                if (photo.getPhotoUrl01() != null) {
                    photo.setThumbnailUrls(photo.getPhotoUrl01());  // 첫 번째 URL을 썸네일로 설정
                }
            }
        }// for*/
    public void savePhotos(PropertyPhotosDto photo) {
        // DB에 사진 저장
        propertyPhotosMapper.insertPhoto(photo);  // DB에 사진 저장
    }

    // 파일 확장자 검증
    private boolean isValidExtension(String fileExtension) {
        List<String> validExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".bmp");
        return validExtensions.contains(fileExtension);
    }
    // 사진 유효성 검사
    public void validatePhoto(MultipartFile photo) {
        long maxSize = 5 * 1024 * 1024;  // 최대 5MB
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

    // 사진 삭제 (파일과 DB에서 삭제)
    public void deletePhotos(List<Long> residNos) {
        for (Long residNo : residNos) {
            List<String> photoFiles = propertyPhotosMapper.getPhotoFilesByResidNo(residNo);
            for (String fileName : photoFiles) {
                try {
                    deletePhotoFile(fileName);  // 파일 삭제
                } catch (IOException e) {
                    log.error("사진 파일을 삭제하지 못했습니다.: {}", fileName, e);
                }
            }
            propertyPhotosMapper.deletePhoto(residNo);  // DB에서 사진 정보 삭제
        }
    }
    // 사진 삭제 처리
    private void deletePhotoFile(String fileName) throws IOException {
        Path filePath = getFilePath(fileName);
        File file = new File(filePath.toUri());
        if (file.exists()) {
            if (file.delete()) {
                log.info("삭제된 사진 파일: {}", fileName);
            } else {
                log.warn("사진 파일을 삭제하지 못했습니다.: {}", fileName);
            }
        }
    }

    // 사진 수정 메서드
    public void updatePhoto(Long residNo, List<MultipartFile> photos) throws IOException {
        // 1. 기존 사진을 삭제 (파일 및 DB에서 삭제)
        List<String> currentPhotoFiles = propertyPhotosMapper.getPhotoFilesByResidNo(residNo);
        List<PropertyPhotosDto> existingPhotos = propertyPhotosMapper.getPhotosByResidNo(residNo);

        // 기존 사진을 DB에서 삭제
        propertyPhotosMapper.deletePhoto(residNo);

        // 기존 사진 파일 삭제
        for (String fileName : currentPhotoFiles) {
            try {
                deletePhotoFile(fileName); // 파일 삭제
            } catch (IOException e) {
                log.error("사진 파일을 삭제하지 못했습니다.: {}", fileName, e);
            }
        }

        // 2. 새로운 사진 저장
        boolean isFirstFile = true;
        PropertyPhotosDto propertyPhotos = new PropertyPhotosDto();
        propertyPhotos.setResidNo(residNo);  // 외래 키 설정

        for (int i = 0; i < photos.size(); i++) {
            MultipartFile file = photos.get(i);
            log.info("파일 처리 중: {}", file.getOriginalFilename());

            // 파일명 생성 및 저장
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            String savedFileName = savePhoto(file, fileName, residNo);
            String encodedFileName = URLEncoder.encode(savedFileName, "UTF-8");

            // 첫 번째 파일을 썸네일로 설정
            if (isFirstFile) {
                propertyPhotos.setThumbnailUrls(encodedFileName);
                isFirstFile = false;
            }

            // 각 사진 URL을 photoUrl01 ~ photoUrl10에 채워 넣음
            switch (i) {
                case 0: propertyPhotos.setPhotoUrl01(encodedFileName); break;
                case 1: propertyPhotos.setPhotoUrl02(encodedFileName); break;
                case 2: propertyPhotos.setPhotoUrl03(encodedFileName); break;
                case 3: propertyPhotos.setPhotoUrl04(encodedFileName); break;
                case 4: propertyPhotos.setPhotoUrl05(encodedFileName); break;
                case 5: propertyPhotos.setPhotoUrl06(encodedFileName); break;
                case 6: propertyPhotos.setPhotoUrl07(encodedFileName); break;
                case 7: propertyPhotos.setPhotoUrl08(encodedFileName); break;
                case 8: propertyPhotos.setPhotoUrl09(encodedFileName); break;
                case 9: propertyPhotos.setPhotoUrl10(encodedFileName); break;
            }
        }

        // DB에 사진 저장
        // photoNo 값을 기존 사진에서 찾아서 처리
        Long photoNo = existingPhotos.isEmpty() ? null : existingPhotos.get(0).getPhotoNo(); // 첫 번째 사진의 photoNo를 사용
        propertyPhotosMapper.updatePhoto(residNo, photoNo);  // photoNo는 해당하는 사진의 ID
    }

}