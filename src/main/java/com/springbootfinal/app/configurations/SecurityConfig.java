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
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

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
                        .requestMatchers("/static/**", "/bootstrap/**", "/css/**", "/js/**", "/images/**", "/joinResult", "/h2-console/**", "/userInfo").permitAll()
                        .requestMatchers("/hostJoinResult", "/hostUserJoin").permitAll()
                        .requestMatchers("/main", "/weather", "/weatherResult", "/accomSearch", "/search", "/detail1/**", "/transfers", "/inquiries").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/host/**").hasAnyRole("HOST", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureHandler(authenticationFailureHandler())
                        .permitAll()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureHandler(authenticationFailureHandler())
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendRedirect("/login?error=unauthenticated");
                        })
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

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            log.info("로그인 실패: " + exception.getMessage());
            String errorMessage = "로그인에 실패했습니다. 다시 시도해주세요.";
            if (exception.getMessage().contains("Bad credentials")) {
                errorMessage = "아이디 또는 비밀번호가 잘못되었습니다.";
            }
            request.getSession().setAttribute("loginError", errorMessage);
            response.sendRedirect("/login?error=true");
        };
    }
}