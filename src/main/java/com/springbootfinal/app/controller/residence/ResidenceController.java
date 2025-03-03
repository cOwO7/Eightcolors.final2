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
import com.springbootfinal.app.service.room.ReservationService;
import com.springbootfinal.app.service.weather.AllWeatherService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
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
    private final ReservationService reservationService;



    @Autowired
    public ResidenceController(
            AllWeatherService allWeatherService,
            ResidenceService residenceService,
            PropertyPhotosService propertyPhotosService,
            PropertyPhotoMapper propertyPhotosMapper,
            ResidenceRoomMapper residenceRoomMapper,
            ResidenceRoomService residenceRoomService,
            ReservationService reservationService) {
        this.allWeatherService = allWeatherService;
        this.residenceService = residenceService;
        this.propertyPhotosService = propertyPhotosService;
        this.propertyPhotosMapper = propertyPhotosMapper;
        this.residenceRoomMapper = residenceRoomMapper;
        this.residenceRoomService = residenceRoomService;
        this.reservationService=reservationService;
    }

    // 숙소 목록 조회
    @GetMapping("/list")
    public String listResidences(Model model) {
        List<ResidenceDto> residences = residenceService.getAllResidences();
        //List<ResidenceRoom> rooms = new ArrayList<>();
        model.addAttribute("residences", residences);
        return "views/residence/Residence"; // "Residence" 페이지에 데이터를 전달
    }
    // 업자 목록 조회
    @GetMapping("/list/{hostUserNo}")
    public String listPosts(@PathVariable Long hostUserNo, Model model) {
        System.out.println("Received hostUserNo: " + hostUserNo);
        List<ResidenceDto> residences = residenceService.findPostsByHostUserNo(hostUserNo);
        model.addAttribute("residences", residences);
        if (!residences.isEmpty()) {
            model.addAttribute("residNo",  residences.get(0).getResidNo());
            // 추가 로직
        }

        return "views/residence/Residence1";
    }

    // 사용자숙소 상세정보
    @GetMapping("/detail1/{residNo}")
    public String viewResidence1(@PathVariable Long residNo,
                                @RequestParam(value="searchKeyword",required = false) String searchKeyword,
                                @RequestParam(name = "checkinDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkinDate,
                                @RequestParam(name = "checkoutDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkoutDate,
                                Model model,HttpSession session) {
        // 숙소 정보를 가져옴
        var residence = residenceService.getResidenceById(residNo);
        List<ResidenceRoom> rooms = residenceService.getRoomsByResidenceId(residNo);

        //예약된 방 리스트 번호 조회
        List<Long> selectReservedRoomNos = reservationService.selectReservedRoomNos(checkinDate, checkoutDate);
        Long userNo = (Long) session.getAttribute("userNo");
        if(userNo!=null){
            model.addAttribute("userNo",userNo);
        }


        // 모델에 데이터를 추가
        model.addAttribute("residence", residence);
        model.addAttribute("rooms",rooms);

        model.addAttribute("checkinDate", checkinDate);
        model.addAttribute("checkoutDate", checkoutDate);
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("selectReservedRoomNos",selectReservedRoomNos);
        // 상세 보기 페이지 반환
        return "views/residence/userResidenceDetail"; // 뷰 파일로 이동
    }



    // 숙소 상세정보
    @GetMapping("/detail/{residNo}")
    public String viewResidence(@PathVariable Long residNo,
                                Model model) {
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
                                  @RequestParam("photoFiles") MultipartFile[] photoFiles,
                                  HttpSession httpSession) {
        residence.setHostUserNo((Long)httpSession.getAttribute("hostUserNo"));
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
            Long hostUserNo = (Long) httpSession.getAttribute("hostUserNo");
            return "redirect:/list/" + hostUserNo;  // 목록 페이지로 리다이렉트
        } catch (IOException e) {
            log.error("File upload error: ", e);
            return "redirect:/error";  // 오류 처리
        } catch (IllegalArgumentException e) {
            log.error("Invalid input error: ", e);
            return "redirect:/error";  // 잘못된 입력 오류 처리
        }
    }

    // 숙소 수정 페이지
    @GetMapping("/update/{residNo}")
    public String editResidenceForm(@PathVariable Long residNo, Model model) {
        // 숙소 정보 불러오기
        ResidenceDto residence = residenceService.getResidenceById(residNo);
        List<PropertyPhotosDto> photos = propertyPhotosService.getPhotosByResidenceId(residNo);

        model.addAttribute("residence", residence);
        model.addAttribute("photos", photos);

        return "views/residence/ResidenceUpdate"; // 수정 페이지
    }

    // 숙소 수정 처리
    /*@PostMapping("/update/{residNo}")
    public String updateResidence(@PathVariable Long residNo,
                                  @ModelAttribute ResidenceDto residence,
                                  @RequestParam(value = "photos", required = false)
                                      List<MultipartFile> photos,
                                  @RequestParam(value = "deletedPhotos", required = false)
                                      String deletedPhotos,
                                  HttpSession session) throws IOException {
        // 기존 숙소 정보 가져오기
        ResidenceDto existingResidence = residenceService.getResidenceById(residNo);

        // 기존의 hostUserNo와 hostUserName을 유지
        residence.setHostUserNo(existingResidence.getHostUserNo());
        residence.setHostUserName(existingResidence.getHostUserName());

        // 1. 기존 사진 삭제 처리 (삭제된 사진 ID 처리)
        if (deletedPhotos != null && !deletedPhotos.isEmpty()) {
            String[] deletedIds = deletedPhotos.split(",");
            for (String photoId : deletedIds) {
                Long photoNo = Long.parseLong(photoId);  // deletedPhotos에 담긴 각 ID로 사진 삭제
                propertyPhotosService.deletePhotos(photoNo);  // 사진 파일과 DB에서 삭제
            }
        }
        // 2. 새로운 사진이 있을 경우 photo update 실행
        if (photos != null && !photos.isEmpty()) {
            residenceService.updateResidence(residence, residNo, photos);
        } else {
            // 교체 이미지가 없을 때 이미지 수정 없이 업데이트
            residenceService.updateResidence(residence, residNo, null);

        }
        // 2. ResidenceDto의 새로운 사진 URL 리스트에 추가
        //residence.setNewPhotoUrls(new ArrayList<>());  // 기존 사진 처리 후 새로 추가된 사진만
        // 3. Residence 정보 업데이트
        residenceService.updateResidence(residence, residNo, photos);  // photos를 전달하여 처리
        // 4. Session에서 hostUserNo 갖고오기
        Long hostUserNo = (Long) session.getAttribute("hostUserNo");
        // 업데이트 완료 후 목록으로 리다이렉트
        return "redirect:/list" + hostUserNo;
    }*/
    @PostMapping("/update/{residNo}")
    public String updateResidence(@PathVariable Long residNo,
                                  @ModelAttribute ResidenceDto residence,
                                  @RequestParam(value = "photos", required = false)
                                  List<MultipartFile> photos,
                                  @RequestParam(value = "deletedPhotos", required = false)
                                  String deletedPhotos,
                                  HttpSession session) throws IOException {
        // 기존 숙소 정보 가져오기
        ResidenceDto existingResidence = residenceService.getResidenceById(residNo);

        // 기존의 hostUserNo와 hostUserName을 유지
        residence.setHostUserNo(existingResidence.getHostUserNo());
        residence.setHostUserName(existingResidence.getHostUserName());

        // 1. 기존 사진 삭제 처리 (삭제된 사진 ID 처리)
        if (deletedPhotos != null && !deletedPhotos.isEmpty()) {
            String[] deletedIds = deletedPhotos.split(",");
            for (String photoId : deletedIds) {
                Long photoNo = Long.parseLong(photoId);  // deletedPhotos에 담긴 각 ID로 사진 삭제
                propertyPhotosService.deletePhotos(photoNo);  // 사진 파일과 DB에서 삭제
            }
        }

        // 2. 새로운 사진이 있을 경우 photo update 실행
        if (photos != null && !photos.isEmpty()) {
            // 사진이 있을 경우만 update
            residenceService.updateResidence(residence, residNo, photos);
        } else {
            // 사진이 없으면 본문만 수정
            residenceService.updateResidence(residence, residNo, null);
        }

        // 3. Session에서 hostUserNo 갖고오기
        Long hostUserNo = (Long) session.getAttribute("hostUserNo");

        // 업데이트 완료 후 목록으로 리다이렉트
        return "redirect:/list/" + hostUserNo;
    }

    // 숙소 삭제
    @PostMapping("/delete/{residNo}")
    public String deleteResidence(@PathVariable Long residNo,
                                    HttpSession session) {
        residenceService.deleteResidence(residNo);
        Long hostUserNo = (Long) session.getAttribute("hostUserNo");
        return "redirect:/list/" + hostUserNo;
    }

    // 숙소 매진 상태 갱신
    @PutMapping("/{residNo}/sold-out")
    public ResponseEntity<String> updateSoldOutStatus(
                                                      @PathVariable Long residNo,
                                                      @RequestParam boolean soldOut,
                                                      Model model) {
        ResidenceDto residence = residenceService.getResidenceById(residNo);

        String message = soldOut ? "숙소가 매진 처리되었습니다." : "숙소 매진 상태가 해제되었습니다.";
        model.addAttribute("residence", residence);
        return ResponseEntity.ok(message);
    }

    // Room
    // 방 등록 페이지
    @GetMapping("/room/{residNo}/addRoom")
    public String newResidenceRoomForm(@PathVariable Long residNo,
                                       Model model) {
        if (residNo == null) {
            return "redirect:/error";  // residNo가 null일 경우 예외 처리
        }

        ResidenceDto residence = residenceService.getResidenceById(residNo);
        if (residence == null) {
            return "redirect:/error";  // 해당 ID의 residence가 존재하지 않으면 오류 페이지로 이동
        }
        residence.setPropertyPhotos(null);  // 숙소 사진을 제외한 정보만 처리
        ResidenceRoom residenceRoom = new ResidenceRoom();
        model.addAttribute("residence", residence);  // 숙소 정보 전달
        model.addAttribute("residenceRoom", residenceRoom);  // 방 정보 전달

        return "views/residence/ResidenceRoomWriter";  // 방 등록 페이지로 이동
    }

    // 방 등록 처리
    @PostMapping("/room/{residNo}/addRoom")
    public String createResidenceRoom(@ModelAttribute ResidenceRoom residenceRoom,
                                      @PathVariable Long residNo,
                                      MultipartFile roomImage,
                                      HttpSession session
                                      ) throws IOException {
        if (residNo == null) {
            throw new IllegalArgumentException("resid_no가 null입니다.");
        }

        ResidenceDto residence = residenceService.getResidenceById(residNo); // 해당 숙소 데이터 가져오기
        if (residence == null) {
            throw new IllegalArgumentException("유효한 숙소 정보가 없습니다.");
        }

        residenceRoom.setResidNo(residNo);  // 방에 해당하는 숙소 번호를 세팅
        residenceRoomService.createResidenceRoom(residenceRoom, roomImage);  // 방 등록

        Long hostUserNo = (Long) session.getAttribute("hostUserNo");
        return "redirect:/list/" + hostUserNo;
    }

    // 방 수정 페이지
    @GetMapping("/update/{residNo}/room")
    public String editResidenceRoomForm(@PathVariable("residNo") Long residNo, Model model) {
        List<ResidenceRoom> residenceRooms = residenceRoomService.getRoomsByResidenceId(residNo);
        List<PropertyPhotosDto> photos = propertyPhotosService.getPhotosByResidenceId(residNo);

        // residence 객체를 추가해야 함
        ResidenceDto residence = residenceService.getResidenceById(residNo);

        model.addAttribute("residenceRooms", residenceRooms);
        model.addAttribute("photos", photos);
        model.addAttribute("resid", residNo);
        model.addAttribute("residence", residence);  // 추가

        return "views/residence/ResidenceRoomUpdate";
    }

    // 방 수정 처리 (residNo에 해당하는 여러 개의 방을 수정)
    @PostMapping("/update/{residNo}/room")
    public String updateResidenceRooms(@PathVariable("residNo") Long residNo,
                                       @ModelAttribute ResidenceRoom residenceRoom, // 여러 개의 방 데이터를 받기 위한 Wrapper
                                       @RequestParam(value = "roomImages", required = false)
                                           List<MultipartFile> roomImages,
                                       HttpSession session) throws IOException {
        List<ResidenceRoom> residenceRooms = residenceRoom.getResidenceRooms();

        // 각 방에 대해 업데이트 진행
        for (int i = 0; i < residenceRooms.size(); i++) {
            ResidenceRoom room = residenceRooms.get(i);
            MultipartFile roomImage = (roomImages != null && i < roomImages.size()) ? roomImages.get(i) : null;

            residenceRoomService.updateRoom(room, residNo, room.getRoomNo(), roomImage);
        }

        Long hostUserNo = (Long) session.getAttribute("hostUserNo");
        return "redirect:/list/" + hostUserNo;
    }

    // 방 삭제
    @GetMapping("/delete/{residNo}/rooms")
    public String showDeleteRoomForm(@PathVariable Long residNo, Model model) {
        List<ResidenceRoom> rooms = residenceRoomService.getRoomsByResidenceId(residNo);
        model.addAttribute("rooms", rooms);
        model.addAttribute("residNo", residNo);
        return "views/residence/ResidenceRoomDelete"; // 삭제 폼 뷰 이름
    }

    // 개별 삭제
    @PostMapping("/delete/{residNo}/room")
    public String deleteSelectedRooms(@PathVariable Long residNo,
                                      @RequestParam(required = false) List<Long> roomNos,
                                      RedirectAttributes redirectAttributes) {
        if (roomNos == null || roomNos.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "삭제할 방을 선택하세요.");
            return "redirect:/delete/" + residNo + "/rooms"; // 다시 삭제 폼으로 이동
        }

        residenceRoomService.deleteRooms(roomNos); // 여러 개 삭제 실행

        redirectAttributes.addFlashAttribute("message", "선택한 방이 삭제되었습니다.");
        return "redirect:/delete/" + residNo + "/rooms"; // 삭제 후 리다이렉트
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
