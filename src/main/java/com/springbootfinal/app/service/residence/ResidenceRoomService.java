package com.springbootfinal.app.service.residence;

import com.springbootfinal.app.domain.residence.ResidenceRoom;
import com.springbootfinal.app.mapper.residence.ResidenceRoomMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
public class ResidenceRoomService {

    @Value("${UPLOAD_DIR}")
    private String UPLOAD_DIR;

    private final ResidenceRoomMapper residenceRoomMapper;
    private final PropertyPhotosService propertyPhotosService;

    @Autowired
    public ResidenceRoomService(ResidenceRoomMapper residenceRoomMapper, PropertyPhotosService propertyPhotosService) {
        this.residenceRoomMapper = residenceRoomMapper;
        this.propertyPhotosService = propertyPhotosService;
    }

    /*// 방 등록
    public void createResidenceRoom (ResidenceRoom residenceRoom) {

        residenceRoomMapper.insertRoom(residenceRoom);
        //Long residNo = residence.getResidNo();

    }

    // 방 수정
    public void updateRoom(ResidenceRoom residenceRoom, Long residNo, Long roomNo) {
        // residNo와 roomNo를 기준으로 방을 업데이트
        residenceRoom.setResidNo(residNo);  // 숙소 번호
        residenceRoom.setRoomNo(roomNo);    // 방 번호

        // 방을 업데이트하는 Mapper 호출
        residenceRoomMapper.updateRoom(residenceRoom);
    }*/

    // 방 등록
    @Transactional
    public void createResidenceRoom(ResidenceRoom residenceRoom, MultipartFile roomImage) throws IOException {
        // 1. 방 정보 먼저 저장 (roomNo가 DB에서 생성됨)
        residenceRoomMapper.insertRoom(residenceRoom);

        // 2. 생성된 roomNo 가져오기 (useGeneratedKeys=true 설정 덕분에 가능)
        Long roomNo = residenceRoom.getRoomNo();
        if (roomNo == null) {
            throw new IllegalStateException("방 정보 저장 후 roomNo가 null입니다.");
        }

        // 3. 이미지 저장 (room_url01) - 방 정보 저장 후에 처리
        if (roomImage != null && !roomImage.isEmpty()) {
            String savedFileName = saveRoomPhoto(roomNo, roomImage);

            // 4. 저장된 파일명을 업데이트
            residenceRoom.setRoomUrl01(savedFileName);
            residenceRoomMapper.updateRoom(residenceRoom); // room_url01 업데이트
        }

        log.info("방 정보에 설정된 roomUrl01: {}", residenceRoom.getRoomUrl01());
    }



    // 방 수정
    public void updateRoom(ResidenceRoom residenceRoom, Long residNo, Long roomNo, MultipartFile roomImage) throws IOException {
        // 1. 기존 방 정보 유지
        residenceRoom.setResidNo(residNo);
        residenceRoom.setRoomNo(roomNo);

        // 2. 기존 이미지 가져오기
        String oldImageUrl = residenceRoomMapper.getRoomImageByRoomNo(roomNo);

        if (roomImage != null && !roomImage.isEmpty()) {
            // 기존 이미지 삭제
            if (oldImageUrl != null) {
                propertyPhotosService.deletePhotoFile(oldImageUrl);
            }

            // 새 이미지 저장
            String fileName = UUID.randomUUID().toString() + "_" + roomImage.getOriginalFilename();
            String savedFileName = propertyPhotosService.savePhoto(roomImage, fileName, residNo);

            residenceRoom.setRoomUrl01(savedFileName); // 새로운 이미지 설정
        } else {
            // 새로운 이미지가 없으면 기존 이미지 유지
            residenceRoom.setRoomUrl01(oldImageUrl);
        }

        log.info("업데이트할 roomUrl01: {}", residenceRoom.getRoomUrl01()); // 디버깅용 로그

        // 3. 방 정보 업데이트 실행
        residenceRoomMapper.updateRoom(residenceRoom);
    }


    // 방 삭제
    public void deleteRoom (Long residNo) {
        deleteRoomPhoto(residNo);
        residenceRoomMapper.deleteRoom(residNo);
    }

    public List<ResidenceRoom> getRoomsByResidenceId(Long residNo) {
        return residenceRoomMapper.getRoomsByResidenceId(residNo);
    }

    // 방 사진 관리
    public String saveRoomPhoto(Long roomNo, MultipartFile photo) throws IOException {
        if (photo.isEmpty()) {
            throw new IllegalArgumentException("사진이 비어 있습니다.");
        }
        validatePhoto(photo);

        String fileName = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);

        Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        log.info("방 사진 저장 완료: {}", fileName);

        // DB 업데이트 (updateRoom을 활용)
        ResidenceRoom room = new ResidenceRoom();
        room.setRoomNo(roomNo);
        room.setRoomUrl01(fileName);
        residenceRoomMapper.updateRoom(room);

        return fileName;
    }

    // 방 사진 삭제 및 DB 업데이트
    public void deleteRoomPhoto(Long roomNo) {
        String existingPhoto = residenceRoomMapper.getRoomImageByRoomNo(roomNo);
        if (existingPhoto != null) {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(existingPhoto);
            try {
                Files.deleteIfExists(filePath);
                log.info("방 사진 삭제 완료: {}", existingPhoto);
            } catch (IOException e) {
                log.error("방 사진 삭제 실패: {}", existingPhoto, e);
            }
        }

        // DB에서 이미지 정보 제거 (updateRoom 활용)
        ResidenceRoom room = new ResidenceRoom();
        room.setRoomNo(roomNo);
        room.setRoomUrl01(null);
        residenceRoomMapper.updateRoom(room);
    }

    // 사진 유효성 검사
    private void validatePhoto(MultipartFile photo) {
        long maxSize = 5 * 1024 * 1024;
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif");

        if (photo.getSize() > maxSize) {
            throw new IllegalArgumentException("파일 크기가 너무 큽니다.");
        }

        String originalFileName = photo.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();
        if (!allowedExtensions.contains(fileExtension)) {
            throw new IllegalArgumentException("지원되지 않는 파일 형식입니다.");
        }
    }
}
