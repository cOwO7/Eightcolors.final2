package com.springbootfinal.app.service.residence;

import com.springbootfinal.app.domain.residence.PropertyPhotosDto;
import com.springbootfinal.app.domain.residence.ResidenceRoom;
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
import java.util.*;

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
        return Paths.get(UPLOAD_DIR).resolve(fileName);
    }

    // 저장
    public String savePhoto(MultipartFile photo, String fileName, Long residNo) throws IOException {
        /*if (photo.isEmpty()) {
            throw new IllegalArgumentException("사진이 비어 있습니다.");
        }*/
        validatePhoto(photo);
        String originalFileName = photo.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();

        if (!isValidExtension(fileExtension)) {
            throw new IllegalArgumentException("허용되지 않은 파일 확장자입니다.");
        }

        if (fileName.toLowerCase().endsWith(fileExtension)) {
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        }

        String fullFileName = fileName + fileExtension;
        Path filePath = getFilePath(fullFileName);

        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        log.info("저장된 사진: {}", fullFileName);
        return fullFileName;
    }

    // 저장 처리
    public void savePhotos(PropertyPhotosDto photo) {
        propertyPhotosMapper.insertPhoto(photo);
    }

    // 확장자 검사 처리
    private boolean isValidExtension(String fileExtension) {
        List<String> validExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".bmp");
        return validExtensions.contains(fileExtension);
    }
    // 확장자 검사
    public void validatePhoto(MultipartFile photo) {
        long maxSize = 5 * 1024 * 1024;
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

    public void deletePhotos(Long photoNo) {
        // 1. 파일 경로를 가져오기 (예: photo_url01~10에서)
        List<String> photoFiles = propertyPhotosMapper.getPhotoUrlsByPhotoNo(photoNo);  // photoNo에 해당하는 파일 경로 가져오기

        // 2. 파일 삭제 처리
        for (String fileName : photoFiles) {
            try {
                deletePhotoFile(fileName);  // 실제 파일 삭제
            } catch (IOException e) {
                log.error("사진 파일을 삭제하지 못했습니다: {}", fileName, e);
            }
        }
        // 3. DB에서 해당 사진 정보 삭제
        propertyPhotosMapper.deletePhoto(photoNo);  // photoNo에 해당하는 레코드 삭제
    }

    public void deletePhotoFile(String fileName) throws IOException {
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

    public void updatePhoto(Long residNo, List<MultipartFile> photos) throws IOException {
        // 1. 기존 사진 파일 URL을 가져온다.
        List<String> currentPhotoFiles = propertyPhotosMapper.getPhotoUrlsByPhotoNo(residNo);
        List<PropertyPhotosDto> existingPhotos = propertyPhotosMapper.getPhotosByResidNo(residNo);

        // 기존 사진 파일 삭제
        for (String fileName : currentPhotoFiles) {
            try {
                deletePhotoFile(fileName);  // 실제 파일 삭제
            } catch (IOException e) {
                log.error("사진 파일을 삭제하지 못했습니다.: {}", fileName, e);
            }
        }

        // 2. 새로운 사진 정보 저장
        boolean isFirstFile = true;
        PropertyPhotosDto propertyPhotos = new PropertyPhotosDto();
        propertyPhotos.setResidNo(residNo);  // 사진이 속한 거주지 ID 설정
        Set<String> processedFileNames = new HashSet<>();  // 이미 처리된 파일명 추적
        // 3. 사진 파일을 하나씩 처리
        for (int i = 0; i < photos.size(); i++) {
            MultipartFile file = photos.get(i);
            log.info("파일 처리 중: {}", file.getOriginalFilename());

            // 파일 이름 생성 및 저장
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // 중복 파일 처리: 이미 처리된 파일은 건너뛰기
            if (processedFileNames.contains(fileName)) {
                log.info("중복된 파일이므로 건너뜁니다: {}", file.getOriginalFilename());
                continue;  // 이미 처리된 파일은 건너뛰기
            }
            String savedFileName = savePhoto(file, fileName, residNo);  // 파일을 서버에 저장
            String encodedFileName = URLEncoder.encode(savedFileName, "UTF-8");  // URL 인코딩
            processedFileNames.add(savedFileName);  // 처리된 파일을 기록
            // 첫 번째 파일을 썸네일로 설정
            if (isFirstFile) {
                propertyPhotos.setThumbnailUrls(encodedFileName);
                isFirstFile = false;
            }
            // 각 사진 URL을 설정 (최대 10개까지 처리)
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
        // 4. 기존 사진 정보가 있으면 수정, 없으면 새로 삽입
        if (!existingPhotos.isEmpty()) {
            // 기존 사진 정보가 있으면 업데이트
            propertyPhotos.setPhotoNo(existingPhotos.get(0).getPhotoNo());  // 기존 사진 번호를 설정
            propertyPhotosMapper.updatePhoto(propertyPhotos);  // 업데이트
        }
    }

    public List<PropertyPhotosDto> getPhotosByResidenceId(Long residNo) {
        return propertyPhotosMapper.getPhotosByResidNo(residNo);
    }

    public String saveRoom(Long roomNo,
                           MultipartFile photo,
                           String fileName) {
        ResidenceRoom residenceRoom = new ResidenceRoom();
        residenceRoom.setRoomNo(roomNo);


        return propertyPhotosMapper.insertRoom(roomNo);
    }
}