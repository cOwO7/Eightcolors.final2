package com.springbootfinal.app.controller.login;

import com.springbootfinal.app.domain.login.Users;
import com.springbootfinal.app.service.login.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Slf4j
@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/clear-error-messages")
    public void clearErrorMessages(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("loginError");
        session.removeAttribute("socialLoginError");
        log.info("Error messages cleared from session");
    }

    @ExceptionHandler(OAuth2AuthenticationException.class)
    public String handleOAuth2AuthenticationException(OAuth2AuthenticationException ex, RedirectAttributes redirectAttributes) {
        log.error("OAuth2AuthenticationException 발생: " + ex.getError().getErrorCode() + " - " + ex.getMessage());
        String error = "소셜 로그인 중 오류가 발생했습니다.";
        if ("email_exists".equals(ex.getError().getErrorCode())) {
            error = "동일한 이메일로 가입된 로컬 계정이 존재합니다.";
        } else if ("provider_mismatch".equals(ex.getError().getErrorCode())) {
            error = "동일한 이메일로 다른 소셜 로그인 제공자가 존재합니다.";
        } else {
            error = "알 수 없는 오류가 발생했습니다.";
        }
        log.error("설정된 에러 메시지: " + error);
        redirectAttributes.addFlashAttribute("error", error);
        log.error("Flash attribute 설정 완료: error={}", error);
        return "redirect:/loginPage";
    }
}