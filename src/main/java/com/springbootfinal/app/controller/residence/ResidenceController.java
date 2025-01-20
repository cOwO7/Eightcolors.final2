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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Controller
//@RequestMapping("/residence")
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
    public String createResidence(@ModelAttribute ResidenceDto residence,
                                  @RequestParam("photoFiles") MultipartFile[] photoFiles) throws IOException {
        log.info("Received ResidenceDto: {}", residence);  // 디버깅 로그 추가

        try {
            // 사진 파일 처리
            List<String> fileNames = new ArrayList<>();
            for (MultipartFile file : photoFiles) {
                // 파일 이름 중복 방지를 위해 UUID를 이용한 고유 이름 생성
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

                // 파일 저장 시 두 번째 인자 fileName을 전달
                String savedFileName = propertyPhotosService.savePhoto(file, fileName, residence.getResidNo()); // 사진 저장
                fileNames.add(savedFileName);  // 저장된 파일명 리스트에 추가
            }

            // 저장된 파일명들을 ResidenceDto에 추가 (사진 파일 경로 리스트 설정)
            residence.setPhotoUrls(fileNames);

            // PropertyPhotosDto 생성
            PropertyPhotosDto photoDto = new PropertyPhotosDto();
            photoDto.setResidNo(residence.getResidNo());  // 자동 생성된 residNo 사용
            photoDto.setThumbnailUrls(fileNames.get(0));  // 썸네일 URL 설정 (첫 번째 이미지 예시)

            // 사진들을 PropertyPhotosDto에 추가
            for (int i = 0; i < fileNames.size(); i++) {
                switch (i) {
                    case 0: photoDto.setPhotoUrl01(fileNames.get(i)); break;
                    case 1: photoDto.setPhotoUrl02(fileNames.get(i)); break;
                    case 2: photoDto.setPhotoUrl03(fileNames.get(i)); break;
                    case 3: photoDto.setPhotoUrl04(fileNames.get(i)); break;
                    case 4: photoDto.setPhotoUrl05(fileNames.get(i)); break;
                    case 5: photoDto.setPhotoUrl06(fileNames.get(i)); break;
                    case 6: photoDto.setPhotoUrl07(fileNames.get(i)); break;
                    case 7: photoDto.setPhotoUrl08(fileNames.get(i)); break;
                    case 8: photoDto.setPhotoUrl09(fileNames.get(i)); break;
                    case 9: photoDto.setPhotoUrl10(fileNames.get(i)); break;
                }
            }

            propertyPhotosService.savePhotos(new ArrayList<>(List.of(photoDto))); // 사진 테이블에 저장

            return "redirect:/list";  // 등록 후 목록 페이지로 리다이렉트
        } catch (IOException e) {
            log.error("File upload error: ", e);
            return "redirect:/error";  // 에러 발생 시 처리
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
