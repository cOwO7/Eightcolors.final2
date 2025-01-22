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
                        .successHandler(customAuthenticationSuccessHandler) // 로그인 성공 핸들러 설정
                        .failureHandler((request, response, exception) -> {
                            log.info("로그인 실패");
                            response.sendRedirect("/login?error=true");
                        })
                        .permitAll()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .successHandler(customAuthenticationSuccessHandler) // OAuth2 로그인 성공 핸들러 설정
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                )
                .sessionManagement(session -> session
                        .sessionFixation().none() // 세션 고정 방지
                        .maximumSessions(1) // 최대 세션 수
                        .maxSessionsPreventsLogin(false) // 이전 세션 만료 후 새 세션 허용
                        .expiredSessionStrategy(event -> {
                            log.info("Session expired for: " + event.getSessionInformation().getPrincipal());
                            // 세션 만료 후 리다이렉트
                            event.getRequest().getSession().setAttribute("sessionExpired", true);
                            event.getRequest().getRequestDispatcher("/login?expired=true").forward(event.getRequest(), event.getResponse());
                        })
                )
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessUrl("/login?logout")
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