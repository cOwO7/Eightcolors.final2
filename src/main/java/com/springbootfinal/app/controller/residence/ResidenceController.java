package com.springbootfinal.app.controller.residence;

import com.springbootfinal.app.domain.residence.PropertyPhotosDto;
import com.springbootfinal.app.domain.residence.ResidenceDto;
import com.springbootfinal.app.domain.weather.AllWeatherDto;
import com.springbootfinal.app.domain.weather.WeatherDto;
import com.springbootfinal.app.mapper.residence.PropertyPhotoMapper;
import com.springbootfinal.app.service.residence.PropertyPhotosService;
import com.springbootfinal.app.service.residence.ResidenceService;
import com.springbootfinal.app.service.weather.AllWeatherService;
import com.springbootfinal.app.service.weather.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//@RequestMapping("/residence")
@Slf4j
@Controller
public class ResidenceController {

    private final ResidenceService residenceService;
    private final AllWeatherService allWeatherService;
    private final PropertyPhotosService propertyPhotosService;
    private final PropertyPhotoMapper propertyPhotosMapper; // 사진 삭제;

    @Autowired
    public ResidenceController(
            AllWeatherService allWeatherService,
            ResidenceService residenceService,
            PropertyPhotosService propertyPhotosService,
            PropertyPhotoMapper propertyPhotosMapper) {
        this.allWeatherService = allWeatherService;
        this.residenceService = residenceService;
        this.propertyPhotosService = propertyPhotosService;
        this.propertyPhotosMapper = propertyPhotosMapper;
    }

    // 숙소 목록 조회
    @GetMapping("/list")
    public String listResidences(Model model) {
        List<ResidenceDto> residences = residenceService.getAllResidences();

        residences.forEach(residence -> {
            log.info("Residence: {} - Discount Rate: {} - Discounted Price: {}",
                    residence.getResidName(),
                    residence.getDiscountRate(),
                    residence.getDiscountedPrice());
        });
        model.addAttribute("residences", residences);
        return "views/residence/Residence"; // "Residence" 페이지에 데이터를 전달
    }

    @GetMapping("/detail/{residNo}")
    public String viewResidence(@PathVariable Long residNo, Model model) throws IOException {
        // 숙소 정보를 가져옴
        var residence = residenceService.getResidenceById(residNo);

        log.info("residence: {}", residence);

        // 모델에 데이터를 추가
        model.addAttribute("residence", residence);
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
    @PostMapping("/new")
    @Transactional
    public String createResidence(@ModelAttribute ResidenceDto residence,
                                  @RequestParam("photoFiles") MultipartFile[] photoFiles) throws IOException {
        log.info("Received ResidenceDto: {}", residence);

        try {
            residenceService.createResidence(residence, photoFiles);  // 숙소 저장
            Long residNo = residence.getResidNo();
            log.info("Generated residNo: {}", residNo);

            if (residNo == null) {
                throw new IllegalArgumentException("resid_no가 null입니다.");
            }

            if (photoFiles == null || photoFiles.length == 0) {
                log.error("No files received.");
                throw new IllegalArgumentException("사진 파일이 없습니다.");
            }

            List<PropertyPhotosDto> propertyPhotos = new ArrayList<>();
            for (MultipartFile file : photoFiles) {
                log.info("Processing file: {}", file.getOriginalFilename());

                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                String savedFileName = propertyPhotosService.savePhoto(file, fileName, residNo); // 사진 저장
                log.info("Saved file name: {}", savedFileName);

                PropertyPhotosDto photoDto = new PropertyPhotosDto();
                photoDto.setResidNo(residNo);
                photoDto.setThumbnailUrls(savedFileName);

                switch (propertyPhotos.size()) {
                    case 0: photoDto.setPhotoUrl01(savedFileName); break;
                    case 1: photoDto.setPhotoUrl02(savedFileName); break;
                    case 2: photoDto.setPhotoUrl03(savedFileName); break;
                    case 3: photoDto.setPhotoUrl04(savedFileName); break;
                    case 4: photoDto.setPhotoUrl05(savedFileName); break;
                    case 5: photoDto.setPhotoUrl06(savedFileName); break;
                    case 6: photoDto.setPhotoUrl07(savedFileName); break;
                    case 7: photoDto.setPhotoUrl08(savedFileName); break;
                    case 8: photoDto.setPhotoUrl09(savedFileName); break;
                    case 9: photoDto.setPhotoUrl10(savedFileName); break;
                }

                propertyPhotos.add(photoDto);
            }

            propertyPhotosService.savePhotos(propertyPhotos);  // DB에 저장
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
        ResidenceDto residence = residenceService.getResidenceById(residNo);

        // 기존 숙소 데이터와 사진 목록을 함께 모델에 추가
        model.addAttribute("residence", residence);

        return "views/residence/ResidenceUpdate"; // 수정 페이지
    }

    // 숙소 수정 처리
    @PostMapping("/edit/{residNo}")
    public String updateResidence(@PathVariable Long residNo,
                                  @ModelAttribute ResidenceDto residence,
                                  @RequestParam(value = "photos", required = false) List<MultipartFile> photos,
                                  @RequestParam(value = "deletedPhotoIds", required = false) List<Long> deletedPhotoIds) {
        residence.setResidNo(residNo);

        // 기존 숙소 정보 업데이트
        residenceService.updateResidence(residence);

        // 삭제된 사진 처리
        if (deletedPhotoIds != null && !deletedPhotoIds.isEmpty()) {
            propertyPhotosService.deletePhotos(deletedPhotoIds);  // 삭제할 사진 IDs를 받아 처리
        }

        // 새로운 사진 처리
        if (photos != null && !photos.isEmpty()) {
            List<PropertyPhotosDto> newPhotoDtos = new ArrayList<>();
            for (MultipartFile photo : photos) {
                try {
                    // 고유한 파일 이름 생성 (UUID를 사용)
                    String fileName = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();

                    // 파일 저장 시 두 번째 인자 fileName을 전달
                    String savedFileName = propertyPhotosService.savePhoto(photo, fileName, residence.getResidNo()); // 사진 저장

                    PropertyPhotosDto photoDto = new PropertyPhotosDto();
                    photoDto.setResidNo(residNo);  // 기존 숙소의 residNo 설정 (residNo 값이 반드시 설정되어야 함)
                    photoDto.setPhotoUrl01("/uploads/" + savedFileName); // 새 사진 경로 설정
                    newPhotoDtos.add(photoDto);
                } catch (IOException e) {
                    e.printStackTrace();
                    // 에러 처리 로직 추가
                }
            }
            if (!newPhotoDtos.isEmpty()) {
                propertyPhotosService.savePhotos(newPhotoDtos); // 새로운 사진 저장
            }
        }

        return "redirect:/list"; // 수정 완료 후 목록 페이지로 리다이렉트
    }


    // 숙소 삭제
    @PostMapping("/delete/{residNo}")
    public String deleteResidence(@PathVariable Long residNo) {
        propertyPhotosMapper.deletePhoto(residNo); // 사진 삭제
        residenceService.deleteResidence(residNo); // 숙소 삭제
        return "redirect:/list"; // 삭제 후 목록 페이지로 리다이렉트
    }

    // 숙소 매진 상태 갱신
    @PutMapping("/{residNo}/sold-out")
    public ResponseEntity<String> updateSoldOutStatus(@PathVariable Long residNo, @RequestParam boolean soldOut) {
        String message = soldOut ? "숙소가 매진 처리되었습니다." : "숙소 매진 상태가 해제되었습니다.";
        return ResponseEntity.ok(message);
    }

    @PostMapping("/residence/processAllWeather")
    @ResponseBody
    public ResponseEntity<?> processAllWeatherDataJson(
            @RequestBody AllWeatherDto allWeatherDto) throws IOException {

        WeatherDto weatherDto = new WeatherDto(
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
