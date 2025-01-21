package com.springbootfinal.app.service.login;

import com.springbootfinal.app.domain.login.LoginType;
import com.springbootfinal.app.domain.login.Users;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserService userService;
    private final HttpSession httpSession;

    public CustomOAuth2UserService(UserService userService, HttpSession httpSession) {
        this.userService = userService;
        this.httpSession = httpSession;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User;
        try {
            oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        } catch (OAuth2AuthenticationException e) {
            OAuth2Error oauth2Error = new OAuth2Error("oauth2_error", "Failed to load user info from OAuth2 provider", null);
            throw new OAuth2AuthenticationException(oauth2Error, e);
        }

        String provider = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = null;
        String name = null;
        String providerId = null;

        // 사용자 정보 파싱
        if ("google".equals(provider)) {
            providerId = oAuth2User.getName();
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
        } else if ("kakao".equals(provider)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            if (kakaoAccount != null) {
                email = (String) kakaoAccount.get("email");
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                if (profile != null) {
                    name = (String) profile.get("nickname");
                }
            }
            providerId = String.valueOf(attributes.get("id"));
        } else if ("naver".equals(provider)) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            if (response != null) {
                email = (String) response.get("email");
                name = (String) response.get("name");
                providerId = (String) response.get("id");
            }
        }

        if (email == null || name == null) {
            throw new OAuth2AuthenticationException(new OAuth2Error("oauth2_error"), "Email or Name not found from OAuth2 provider");
        }

        // 사용자 정보 디버깅 로그
        System.out.println("OAuth2 Provider: " + provider);
        System.out.println("Provider ID: " + providerId);
        System.out.println("Email: " + email);
        System.out.println("Name: " + name);

        // 사용자 조회 또는 생성
        Users user = userService.findByEmail(email);
        if (user == null) {
            user = userService.saveSocialUser(email, name, providerId, LoginType.valueOf(provider.toUpperCase()));
        }

        // 세션에 로그인 정보 저장
        httpSession.setAttribute("isLogin", true);
        httpSession.setAttribute("role", "user");
        httpSession.setAttribute("userNo", user.getUserNo()); // 유저 번호 세션에 저장

        // 사용자 정보 반환
        Map<String, Object> customAttributes = Map.of(
                "email", email,
                "name", name,
                "providerId", providerId,
                "provider", provider
        );

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                customAttributes,
                "email"
        );
    }
}