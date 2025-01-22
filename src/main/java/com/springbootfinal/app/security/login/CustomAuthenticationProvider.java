package com.springbootfinal.app.security.login;

import com.springbootfinal.app.service.login.AdminUserService;
import com.springbootfinal.app.service.login.HostUserService;
import com.springbootfinal.app.service.login.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    private final UserService userService;
    private final AdminUserService adminUserService;
    private final HostUserService hostUserService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomAuthenticationProvider(UserService userService, AdminUserService adminUserService,
                                        HostUserService hostUserService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.adminUserService = adminUserService;
        this.hostUserService = hostUserService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        log.info("Authenticating user with username: {}", username); // 로그 추가

        UserDetails userDetails = findUserDetails(username);

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            log.info("Password matched for user: {}", username); // 로그 추가
            return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        } else {
            log.warn("Invalid credentials for user: {}", username); // 로그 추가
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    private UserDetails findUserDetails(String username) throws AuthenticationException {
        UserDetails userDetails;

        try {
            // 일반 사용자로 조회 시도
            log.info("Trying to load user by username from UserService: {}", username); // 로그 추가
            userDetails = userService.loadUserByUsername(username);
            if (userDetails != null) {
                return userDetails;
            }
        } catch (UsernameNotFoundException e) {
            log.info("User not found in UserService: {}", username); // 로그 추가
        }

        try {
            // 호스트 사용자로 조회 시도
            log.info("Trying to load user by username from HostUserService: {}", username); // 로그 추가
            userDetails = hostUserService.loadUserByUsername(username);
            if (userDetails != null) {
                return userDetails;
            }
        } catch (UsernameNotFoundException e) {
            log.info("User not found in HostUserService: {}", username); // 로그 추가
        }

        try {
            // 관리자 사용자로 조회 시도
            log.info("Trying to load user by username from AdminUserService: {}", username); // 로그 추가
            userDetails = adminUserService.loadUserByUsername(username);
            if (userDetails != null) {
                return userDetails;
            }
        } catch (UsernameNotFoundException e) {
            log.info("User not found in AdminUserService: {}", username); // 로그 추가
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}