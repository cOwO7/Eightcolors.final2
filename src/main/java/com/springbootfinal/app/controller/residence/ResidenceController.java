package com.springbootfinal.app.controller.residence;

import com.springbootfinal.app.domain.residence.PropertyPhotosDto;
import com.springbootfinal.app.domain.residence.ResidenceDto;
import com.springbootfinal.app.domain.residence.ResidenceRoom;
import com.springbootfinal.app.domain.weather.AllWeatherDto;
import com.springbootfinal.app.domain.weather.WeatherDto;
import com.springbootfinal.app.mapper.residence.PropertyPhotoMapper;
import com.springbootfinal.app.mapper.residence.ResidenceRoomMapper;
import com.springbootfinal.app.service.residence.PropertyPhotosService;
import com.springbootfinal.app.service.residence.ResidenceRoomService;
import com.springbootfinal.app.service.residence.ResidenceService;
import com.springbootfinal.app.service.weather.AllWeatherService;
import com.springbootfinal.app.service.weather.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

//@RequestMapping("/residence")
@Slf4j
@Controller
public class ResidenceController {

    private final ResidenceService residenceService;
    private final AllWeatherService allWeatherService;
    private final PropertyPhotosService propertyPhotosService;
    private final PropertyPhotoMapper propertyPhotosMapper;
    private final ResidenceRoomMapper residenceRoomMapper;
    private final ResidenceRoomService residenceRoomService;

    @Autowired
    public ResidenceController(
            AllWeatherService allWeatherService,
            ResidenceService residenceService,
            PropertyPhotosService propertyPhotosService,
            PropertyPhotoMapper propertyPhotosMapper,
            ResidenceRoomMapper residenceRoomMapper,
            ResidenceRoomService residenceRoomService) {
        this.allWeatherService = allWeatherService;
        this.residenceService = residenceService;
        this.propertyPhotosService = propertyPhotosService;
        this.propertyPhotosMapper = propertyPhotosMapper;
        this.residenceRoomMapper = residenceRoomMapper;
        this.residenceRoomService = residenceRoomService;
    }

    // 숙소 목록 조회
    @GetMapping("/list")
    public String listResidences(Model model) {
        List<ResidenceDto> residences = residenceService.getAllResidences();
        model.addAttribute("residences", residences);
        return "views/residence/Residence"; // "Residence" 페이지에 데이터를 전달
    }

    // 숙소 상세정보
    @GetMapping("/detail/{residNo}")
    public String viewResidence(@PathVariable Long residNo, Model model) throws IOException {
        // 숙소 정보를 가져옴
        var residence = residenceService.getResidenceById(residNo);
        List<ResidenceRoom> rooms = residenceService.getRoomsByResidenceId(residNo);

        // 모델에 데이터를 추가
        model.addAttribute("residence", residence);
        model.addAttribute("rooms",rooms);
        // 상세 보기 페이지 반환
        return "views/residence/ResidenceDetail"; // 뷰 파일로 이동
    }

    // 숙소 등록 페이지
    @GetMapping("/new")
    public String newResidenceForm(Model model) {
        ResidenceDto residence = new ResidenceDto();
        model.addAttribute("residence", residence);  // 모델에 residence 객체 추가
        return "views/residence/ResidenceWriter";  // 숙소 등록 페이지
    }

    // 숙소 등록 처리
    @Transactional
    @PostMapping("/new")
    public String createResidence(@ModelAttribute ResidenceDto residence,
                                  @RequestParam("photoFiles") MultipartFile[] photoFiles) throws IOException {
        log.info("Received ResidenceDto: {}", residence);

        try {
            residenceService.createResidence(residence);  // 숙소 저장
            Long residNo = residence.getResidNo();
            log.info("Generated residNo: {}", residNo);

            if (residNo == null) {
                throw new IllegalArgumentException("resid_no가 null입니다.");
            }

            if (photoFiles == null || photoFiles.length == 0) {
                log.error("No files received.");
                throw new IllegalArgumentException("사진 파일이 없습니다.");
            }

            // 최대 10개의 사진만 처리
            if (photoFiles.length > 10) {
                log.error("Too many files received: {}", photoFiles.length);
                throw new IllegalArgumentException("최대 10개의 파일만 업로드 가능합니다.");
            }

            boolean isFirstFile = true;  // 첫 번째 파일 여부를 추적하는 변수

            PropertyPhotosDto propertyPhotos = new PropertyPhotosDto();
            propertyPhotos.setResidNo(residNo);  // 외래 키 설정

            for (int i = 0; i < photoFiles.length; i++) {
                MultipartFile file = photoFiles[i];
                log.info("Processing file: {}", file.getOriginalFilename());

                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                // 파일 저장 후 경로 인코딩
                String savedFileName = propertyPhotosService.savePhoto(file, fileName, residNo);
                String encodedFileName = URLEncoder.encode(savedFileName, "UTF-8");

                // 첫 번째 파일에만 썸네일을 설정
                if (isFirstFile) {
                    propertyPhotos.setThumbnailUrls(encodedFileName);  // 첫 번째 파일을 썸네일로 설정
                    isFirstFile = false;  // 이후 파일들은 썸네일을 설정하지 않음
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
            // DB에 여러 개의 사진을 저장
            propertyPhotosService.savePhotos(propertyPhotos);

            return "redirect:/list";  // 목록 페이지로 리다이렉트
        } catch (IOException e) {
            log.error("File upload error: ", e);
            return "redirect:/error";  // 오류 처리
        } catch (IllegalArgumentException e) {
            log.error("Invalid input error: ", e);
            return "redirect:/error";  // 잘못된 입력 오류 처리
        }
    }

    // 숙소 수정 페이지
    @GetMapping("/edit/{residNo}")
    public String editResidenceForm(@PathVariable Long residNo, Model model) {
        // 숙소 정보 불러오기
        ResidenceDto residence = residenceService.getResidenceById(residNo);
        List<PropertyPhotosDto> photos = propertyPhotosService.getPhotosByResidenceId(residNo);

        model.addAttribute("residence", residence);
        model.addAttribute("photos", photos);

        return "views/residence/ResidenceUpdate"; // 수정 페이지
    }

    // 숙소 수정 처리
    @PostMapping("/edit/{residNo}")
    public String updateResidence(@PathVariable Long residNo,
                                  @ModelAttribute ResidenceDto residence,
                                  @RequestParam("photos") List<MultipartFile> photos,
                                  @RequestParam(value = "deletedPhotos", required = false)
                                      String deletedPhotos) throws IOException {
        // 1. 기존 사진 삭제 처리 (삭제된 사진 ID 처리)
        if (deletedPhotos != null && !deletedPhotos.isEmpty()) {
            String[] deletedIds = deletedPhotos.split(",");
            for (String photoId : deletedIds) {
                Long photoNo = Long.parseLong(photoId);  // deletedPhotos에 담긴 각 ID로 사진 삭제
                propertyPhotosService.deletePhotos(photoNo);  // 사진 파일과 DB에서 삭제
            }
        }
        // 2. ResidenceDto의 새로운 사진 URL 리스트에 추가
        residence.setNewPhotoUrls(new ArrayList<>());  // 기존 사진 처리 후 새로 추가된 사진만
        // 3. Residence 정보 업데이트
        residenceService.updateResidence(residence, residNo, photos);  // photos를 전달하여 처리
        return "redirect:/list";  // 업데이트 완료 후 목록으로 리다이렉트
    }

    // 숙소 삭제
    @PostMapping("/delete/{residNo}")
    public String deleteResidence(@PathVariable Long residNo) {
        residenceService.deleteResidence(residNo);
        return "redirect:/list";
    }

    // 숙소 매진 상태 갱신
    @PutMapping("/{residNo}/sold-out")
    public ResponseEntity<String> updateSoldOutStatus(@PathVariable Long residNo,
                                                      @RequestParam boolean soldOut) {
        String message = soldOut ? "숙소가 매진 처리되었습니다." : "숙소 매진 상태가 해제되었습니다.";
        return ResponseEntity.ok(message);
    }

    // Room
    // 방 등록 페이지
    @GetMapping("/room/{residNo}/addRoom")
    public String newResidenceRoomForm(Model model) {
        ResidenceRoom residenceRoom = new ResidenceRoom();
        model.addAttribute("residenceRoom", residenceRoom);  // 모델에 residence 객체 추가
        return "views/residence/ResidenceRoomWriter";  // 숙소 등록 페이지
    }

    // 방 등록 처리
    @PostMapping("/room/{residNo}/addRoom")
    public String createResidenceRoom(@ModelAttribute ResidenceRoom residenceRoom,
                                      Long residNo) throws IOException {
        residenceService.getResidenceById(residenceRoom.getResidNo());
        log.info("Generated residNo: {}", residNo);
        if (residNo == null) {
            throw new IllegalArgumentException("resid_no가 null입니다.");
        }
        residenceRoomService.createResidenceRoom(residenceRoom);

        return "redirect:/list";
    }

    // 방 수정 페이지
    @GetMapping("/edit/{roomNo}/rooom")
    public String editResidenceRoomForm(@PathVariable Long roomNo,
                                        @PathVariable Long residNo,
                                        Model model) {
        // 숙소 정보 불러오기
        ResidenceDto residence = residenceService.getResidenceById(residNo);
        List<PropertyPhotosDto> photos = propertyPhotosService.getPhotosByResidenceId(residNo);

        model.addAttribute("residence", residence);
        model.addAttribute("photos", photos);
        model.addAttribute("roomNo", roomNo);

        return "views/residence/ResidenceRoomUpdate"; // 수정 페이지
    }

    // 방 수정 처리
    @PostMapping("/edit/{roomNo}/room")
    public String updateResidenceRoom(@PathVariable Long roomNo,
                                  @ModelAttribute ResidenceRoom residenceRoom) {
        // 3. Residence 정보 업데이트
        residenceRoomService.updateRoom(residenceRoom, roomNo);  // photos를 전달하여 처리
        return "redirect:/list";  // 업데이트 완료 후 목록으로 리다이렉트
    }

    // 방 삭제
    @PostMapping("/delete/{roomNo}/room")
    public String deleteResidenceRoom(@PathVariable Long roomNo
                                      ) {
        residenceRoomService.deleteRoom(roomNo);
        return "redirect:/list";
    }

    // 날씨 데이터
    @PostMapping("/residence/processAllWeather")
    @ResponseBody
    public ResponseEntity<?> processAllWeatherDataJson(
            @RequestBody AllWeatherDto allWeatherDto) throws IOException {

        WeatherDto weatherDto = new WeatherDto (
                allWeatherDto.getBaseDate(),
                allWeatherDto.getBaseTime(),
                allWeatherDto.getNx(),
                allWeatherDto.getNy(),
                allWeatherDto.getLatitudeNum(),
                allWeatherDto.getLongitudeNum()
        );
        Map<String, Map<String, String>> mergedWeatherData = allWeatherService.getMergedWeatherData(
                weatherDto,
                allWeatherDto.getRegId(),
                allWeatherDto.getTmFc(),
                allWeatherDto.getRegIdTemp()
        );
        return ResponseEntity.ok(mergedWeatherData);
    }
}