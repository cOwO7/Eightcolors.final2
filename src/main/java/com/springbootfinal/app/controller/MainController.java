package com.springbootfinal.app.controller;

import com.springbootfinal.app.service.mainpage.MainPageService;
import com.springbootfinal.app.domain.mainpage.MainPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
public class MainController {

	@Autowired
	private MainPageService mainPageService;

	@GetMapping("/userJoin")
	public String userJoin() {
		return "user/userJoin"; // userjoin.html을 반환합니다.
	}

	@GetMapping("/hostUserJoin")
	public String hostUserJoin() {
		return "user/hostUserJoin";
	}

	@GetMapping("/helper")
	public String helper() {
		return "views/helper";
	}

	@GetMapping("/mypage")
	public String mypage() {
		return "views/mypage";
	}

	@GetMapping("/")
	public String home() {
		// 원하는 페이지로 리다이렉트
		return "redirect:/main";
	}

	@GetMapping("/main")
	public String main(Model model) {
		List<MainPage> randomProperties = mainPageService.getRandomProperties();

		// 예외 처리: 데이터가 3개 미만일 경우 기본 데이터 추가
		while (randomProperties.size() < 3) {
			MainPage placeholder = new MainPage();
			placeholder.setId(0L);
			placeholder.setName("Sample Property");
			placeholder.setPhotoUrl01("sample/sample1.jpg");
			randomProperties.add(placeholder);
		}

		model.addAttribute("randomProperties", randomProperties);
		return "main/main"; // 템플릿 파일 경로를 지정합니다.
	}
}