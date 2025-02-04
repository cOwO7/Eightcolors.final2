package com.springbootfinal.app.controller.login;

import com.springbootfinal.app.service.login.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Slf4j
@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @ExceptionHandler(OAuth2AuthenticationException.class)
    public String handleOAuth2AuthenticationException(OAuth2AuthenticationException ex, HttpSession session) {
        String errorCode = ex.getError().getErrorCode();
        String errorMessage = ex.getMessage();
        log.error("OAuth2AuthenticationException 발생: " + errorCode + " - " + errorMessage);

        String error = "소셜 로그인 중 오류가 발생했습니다.";
        if ("email_exists".equals(errorCode)) {
            error = "동일한 이메일로 가입된 로컬 계정이 존재합니다.";
            log.info("email_exists 생성(컨트롤러)");
        } else if ("provider_mismatch".equals(errorCode)) {
            error = "동일한 이메일로 다른 소셜 로그인 제공자가 존재합니다.";
            log.info("provider_mismatch 생성(컨트롤러)");
        } else {
            log.info("알 수 없는 오류 발생");
        }

        log.error("설정된 에러 메시지: " + error);
        session.setAttribute("socialLoginError", error);
        log.info("세션에 설정된 socialLoginError: " + session.getAttribute("socialLoginError")); // 추가된 로그
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(HttpSession session, @RequestParam(value = "error", required = false) String error) {
        log.info("로그인 페이지 접근");
        // 세션 초기화 확인 로그 추가
        if (error == null) {
            session.removeAttribute("socialLoginError");
            log.info("로그인 페이지 세션 초기화");
        } else {
            log.info("로그인 페이지 에러 상태: " + error);
        }
        return "user/login";
    }

    @PostMapping("/clear-error-messages")
    public void clearErrorMessages(HttpSession session) {
        session.removeAttribute("socialLoginError");
        log.info("세션 에러 메시지 초기화");
    }
}