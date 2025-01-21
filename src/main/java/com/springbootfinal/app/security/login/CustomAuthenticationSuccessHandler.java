package com.springbootfinal.app.security.login;

import com.springbootfinal.app.service.login.HostUserService;
import com.springbootfinal.app.service.login.UserService;
import com.springbootfinal.app.domain.login.Users;
import com.springbootfinal.app.domain.login.HostUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final HostUserService hostUserService;

    public CustomAuthenticationSuccessHandler(UserService userService, HostUserService hostUserService) {
        this.userService = userService;
        this.hostUserService = hostUserService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 로그인 성공 시 세션에 로그인 상태와 사용자 역할 저장
        request.getSession().setAttribute("isLogin", true);

        // 로그인 성공 시 세션에 사용자 역할 저장
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            request.getSession().setAttribute("role", "admin");
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_HOST"))) {
            request.getSession().setAttribute("role", "host");
        } else {
            request.getSession().setAttribute("role", "user");
        }

        // 유저 번호 세션에 저장
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            Users userEntity = userService.findById(user.getUsername());
            log.info("User found: {}", userEntity); // 일반 유저 정보 로그 출력
            if (userEntity != null) {
                request.getSession().setAttribute("userNo", userEntity.getUserNo());
            } else {
                log.error("Failed to find user entity for username: " + user.getUsername());
            }
        } else if (principal instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) principal;
            String email = oAuth2User.getAttribute("email");
            Users userEntity = userService.findByEmail(email);
            log.info("OAuth2 User found: {}", userEntity); // OAuth2 유저 정보 로그 출력
            if (userEntity != null) {
                request.getSession().setAttribute("userNo", userEntity.getUserNo());
            } else {
                log.error("Failed to find user entity for email: " + email);
            }
        }

        // 세션 정보 로그 출력
        log.info("User logged in: userNo={}, role={}",
                request.getSession().getAttribute("userNo"),
                request.getSession().getAttribute("role"));

        response.sendRedirect("/main"); // 로그인 성공 후 메인 페이지로 리다이렉트
    }
}