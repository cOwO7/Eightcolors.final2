package com.springbootfinal.app.security.login;

import com.springbootfinal.app.service.login.HostUserService;
import com.springbootfinal.app.service.login.UserService;
import com.springbootfinal.app.domain.login.Users;
import com.springbootfinal.app.domain.login.HostUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
        log.info("CustomAuthenticationSuccessHandler invoked");

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


        // 유저 번호 세션에 저장..
        Users userEntity = userService.getCurrentUser();
        if (userEntity != null) {
            Long userNo = userEntity.getUserNo();
            log.info("Retrieved userNo from Users: {}", userNo);
            request.getSession().setAttribute("userNo", userNo);
        } else {
            log.warn("User entity is null");
        }



        // 세션 정보 로그 출력
        log.info("User logged in: userNo={}, hostUserNo={}, role={}",
                request.getSession().getAttribute("userNo"),
                request.getSession().getAttribute("hostUserNo"),
                request.getSession().getAttribute("role"));

        response.sendRedirect("/main"); // 로그인 성공 후 메인 페이지로 리다이렉트
    }
}