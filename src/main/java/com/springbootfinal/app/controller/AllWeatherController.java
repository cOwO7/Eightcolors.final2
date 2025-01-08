package com.springbootfinal.app.controller;

import com.springbootfinal.app.domain.AllWeatherDto;
import com.springbootfinal.app.domain.WeatherDto;
import com.springbootfinal.app.service.AllWeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
//@RequestMapping("/weather")
public class AllWeatherController {
    private final AllWeatherService allWeatherService;

    @Autowired
    public AllWeatherController(AllWeatherService allWeatherService) {
        this.allWeatherService = allWeatherService;
    }

    @GetMapping("/processAllWeather")
    public String processAllWeatherDataForGet() {
        //return "redirect:/weather/form"; // 적절한 경로로 리다이렉트
        return "redirect:/weatherResult"; // 적절한 경로로 리다이렉트
    }
    // 뷰 이동
    @GetMapping("/weatherResult")
    public String showWeatherResult() {
        return "weather/weatherResult"; // 템플릿 파일 반환
    }

    @PostMapping("/processAllWeather")
    @ResponseBody
    public ResponseEntity<?> processAllWeatherDataJson(@RequestBody AllWeatherDto allWeatherDto) throws IOException {
        WeatherDto weatherDto = new WeatherDto(
                allWeatherDto.getBaseDate(),
                allWeatherDto.getBaseTime(),
                allWeatherDto.getNx(),
                allWeatherDto.getNy()
        );

        Map<String, Map<String, String>> mergedWeatherData = allWeatherService.getMergedWeatherData(
                weatherDto,
                allWeatherDto.getRegId(),
                allWeatherDto.getTmFc(),
                allWeatherDto.getRegIdTemp()
        );

        return ResponseEntity.ok(mergedWeatherData);
    }



    /*@PostMapping("/processAllWeather")
    @ResponseBody
    public ResponseEntity<Map<String, Map<String, String>>> processAllWeatherDataJson(
            @RequestParam String baseDate,
            @RequestParam String baseTime,
            @RequestParam Integer nx,
            @RequestParam Integer ny,
            @RequestParam String regId,
            @RequestParam String tmFc,
            @RequestParam String regIdTemp
    ) {
        try {
            WeatherDto weatherDto = new WeatherDto(baseDate, baseTime, nx, ny);
            Map<String, Map<String, String>> mergedWeatherData =
                    allWeatherService.getMergedWeatherData(weatherDto, regId, tmFc, regIdTemp);
            return ResponseEntity.ok(mergedWeatherData);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }*/

    /*@PostMapping("/processAllWeather")
    @ResponseBody
    public ResponseEntity<Map<String, Map<String, Map<String, String>>>> processAllWeatherDataJson(
            @RequestParam String baseDate,
            @RequestParam String baseTime,
            @RequestParam Integer nx,
            @RequestParam Integer ny,
            @RequestParam String regId,
            @RequestParam String tmFc,
            @RequestParam String regIdTemp
    ) {
        try {
            WeatherDto weatherDto = new WeatherDto(baseDate, baseTime, nx, ny);
            Map<String, Map<String, Map<String, String>>> allWeatherData =
                    allWeatherService.getAllWeatherData(weatherDto, regId, tmFc, regIdTemp);
            return ResponseEntity.ok(allWeatherData);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }*/

    /*@PostMapping("/processAllWeather")
    public String processAllWeatherData(
            @RequestParam String baseDate,
            @RequestParam String baseTime,
            @RequestParam Integer nx,
            @RequestParam Integer ny,
            @RequestParam String regId,
            @RequestParam String tmFc,
            @RequestParam String regIdTemp,
            Model model
    ) {
        try {
            WeatherDto weatherDto = new WeatherDto(baseDate, baseTime, nx, ny);

            // 병합된 데이터 가져오기
            Map<String, Map<String, Map<String, String>>> allWeatherData =
                    allWeatherService.getAllWeatherData(weatherDto, regId, tmFc, regIdTemp);

            model.addAttribute("allWeatherData", allWeatherData);
            return "weather/weatherResult"; // 결과를 보여줄 템플릿 이름
        } catch (IOException e) {
            model.addAttribute("error", "Error fetching weather data: " + e.getMessage());
            return "error"; // 에러 페이지 템플릿 이름
        }
    }*/
}
