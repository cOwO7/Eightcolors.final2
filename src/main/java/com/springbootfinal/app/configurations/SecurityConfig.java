package com.springbootfinal.app.configurations;

import com.springbootfinal.app.custom.CustomAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler
            customAuthenticationSuccessHandler;

    public SecurityConfig(CustomAuthenticationSuccessHandler
                                  customAuthenticationSuccessHandler) {
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();  // 비밀번호 암호화 설정
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http//.authorizeHttpRequests(authorizeHttpRequests ->
                // authorizeHttpRequests
                .csrf().disable() // CSRF 보호 비활성화
                .authorizeRequests()
                .requestMatchers("/login","/**").permitAll()  // 로그인 경로 허용
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .successHandler(customAuthenticationSuccessHandler) // 로그인 성공 시 핸들러 설정
                .loginPage("/login")
                .defaultSuccessUrl("/main", true)
                .failureUrl("/login?error=true")
                .and()
                .logout()
                .logoutSuccessUrl("/login");

        return http.build();
    }
}
/*public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
                          CustomUserDetailsService customUserDetailsService) {
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 암호화 설정
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // CSRF 보호 비활성화
                .authorizeRequests()
                .antMatchers("/login", "/register", "/public/**").permitAll() // 로그인, 회원가입, 공개 경로 허용
                .antMatchers("/admin/**").hasRole("ADMIN") // 관리자 페이지 접근 제어
                .antMatchers("/user/**").hasRole("USER") // 사용자 페이지 접근 제어
                .anyRequest().authenticated() // 나머지 요청은 인증 필요
                .and()
                .formLogin()
                .loginPage("/login") // 사용자 정의 로그인 페이지
                .defaultSuccessUrl("/main", true) // 로그인 성공 시 리디렉션
                .failureUrl("/login?error=true") // 로그인 실패 시 리디렉션
                .and()
                .oauth2Login()
                .successHandler(customAuthenticationSuccessHandler) // OAuth2 로그인 성공 핸들러
                .and()
                .logout()
                .logoutSuccessUrl("/login") // 로그아웃 성공 시 리디렉션
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }
}*/