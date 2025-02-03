package com.springbootfinal.app.configurations;

import com.springbootfinal.app.security.login.CustomAuthenticationProvider;
import com.springbootfinal.app.security.login.CustomAuthenticationSuccessHandler;
import com.springbootfinal.app.service.login.CustomOAuth2UserService;
import com.springbootfinal.app.service.login.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public SecurityConfig(UserService userService, CustomOAuth2UserService customOAuth2UserService, CustomAuthenticationProvider customAuthenticationProvider, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.userService = userService;
        this.customOAuth2UserService = customOAuth2UserService;
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "user/**", "/userJoin", "/login", "/oauth2/**", "/register", "/oauth2.0/*", "/overlapIdCheck").permitAll()
                        .requestMatchers("/static/**", "/bootstrap/**", "/css/**", "/js/**", "/images/**", "/joinResult", "/h2-console/**", "/userInfo", "/hostUserJoin").permitAll()
                        .requestMatchers("/hostJoinResult").permitAll()
                        .requestMatchers("/list", "**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/host/**").hasAnyRole("HOST", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureHandler((request, response, exception) -> {
                            log.info("로그인 실패: " + exception.getMessage());
                            String errorMessage = "로그인에 실패했습니다. 다시 시도해주세요.";
                            if (exception.getMessage().contains("Bad credentials")) {
                                errorMessage = "아이디 또는 비밀번호가 잘못되었습니다.";
                            }
                            request.getSession().setAttribute("loginError", errorMessage);
                            response.sendRedirect("/login?error=true");
                        })
                        .permitAll()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureHandler((request, response, exception) -> {
                            log.info("소셜 로그인 실패: " + exception.getMessage());
                            String errorMessage = "소셜 로그인 중 오류가 발생했습니다.";
                            if (exception.getMessage().contains("email_exists")) {
                                errorMessage = "동일한 이메일로 가입된 로컬 계정이 존재합니다.";
                            } else if (exception.getMessage().contains("provider_mismatch")) {
                                errorMessage = "동일한 이메일로 다른 소셜 로그인 제공자가 존재합니다.";
                            }
                            request.getSession().setAttribute("socialLoginError", errorMessage);
                            response.sendRedirect("/login?error=true");
                        })
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                )
                .sessionManagement(session -> session
                        .sessionFixation().none()
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                        .expiredSessionStrategy(event -> {
                            log.info("Session expired for: " + event.getSessionInformation().getPrincipal());
                            event.getRequest().getSession().setAttribute("sessionExpired", true);
                            event.getRequest().getRequestDispatcher("/login?expired=true").forward(event.getRequest(), event.getResponse());
                        })
                )
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                );

        http.csrf(csrf -> csrf.disable());
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(customAuthenticationProvider)
                .build();
    }
}