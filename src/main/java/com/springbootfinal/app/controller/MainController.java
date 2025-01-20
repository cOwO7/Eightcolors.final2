package com.springbootfinal.app.controller;

import com.springbootfinal.app.service.MainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class MainController {

	@Autowired
	private MainService mainService;

	@GetMapping("/login")
	public String showLoginForm() {
		return "user/login";
	}

	@GetMapping("/userJoin")
	public String userJoin() {
		return "user/userJoin"; // userjoin.html을 반환합니다.
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
	public String main() {
		return "main/main";
	}
}
