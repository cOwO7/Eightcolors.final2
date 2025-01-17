package com.springbootfinal.app.security.login;

import jakarta.servlet.FilterChain;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, Authentication authentication) throws IOException, jakarta.servlet.ServletException {

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
        response.sendRedirect("/main"); // 로그인 성공 후 메인 페이지로 리다이렉트
    }
}