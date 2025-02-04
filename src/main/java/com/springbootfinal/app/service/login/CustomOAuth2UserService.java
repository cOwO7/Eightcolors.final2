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
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;

@Slf4j
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
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = null;
        String name = null;
        String providerId = null;

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
            log.error("Email or Name not found from OAuth2 provider");
            throw new OAuth2AuthenticationException(new OAuth2Error("oauth2_error"), "Email or Name not found from OAuth2 provider");
        }

        Users user = userService.findByEmail(email);
        if (user != null) {
            if ("LOCAL".equals(user.getLoginType())) {
                log.error("동일한 이메일로 가입된 로컬 계정이 존재합니다.");
                log.info("email_exists 생성(서비스)");
                throw new OAuth2AuthenticationException(new OAuth2Error("email_exists"), "동일한 이메일로 가입된 로컬 계정이 존재합니다.");
            } else if (!provider.equalsIgnoreCase(user.getLoginType().name())) {
                log.error("동일한 이메일로 다른 소셜 로그인 제공자가 존재합니다.");
                log.info("provider_mismatch 생성(서비스)");
                throw new OAuth2AuthenticationException(new OAuth2Error("provider_mismatch"), "동일한 이메일로 다른 소셜 로그인 제공자가 존재합니다.");
            }
        } else {
            user = userService.saveSocialUser(email, name, providerId, LoginType.valueOf(provider.toUpperCase()));
        }

        log.info("소셜 로그인 성공 - 이메일: " + email);

        httpSession.setAttribute("isLogin", true);
        httpSession.setAttribute("role", "user");
        httpSession.setAttribute("userNo", user.getUserNo());

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