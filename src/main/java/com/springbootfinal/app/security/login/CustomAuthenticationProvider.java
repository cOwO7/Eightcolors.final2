package com.springbootfinal.app.security.login;

import com.springbootfinal.app.service.admin.AdminUserService;
import com.springbootfinal.app.service.host.HostUserService;
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

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

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

        UserDetails userDetails = findUserDetails(username);

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        } else {
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    private UserDetails findUserDetails(String username) throws AuthenticationException {
        UserDetails userDetails;

        try {
            // 일반 사용자로 조회 시도
            userDetails = userService.loadUserByUsername(username);
            if (userDetails != null) {
                return userDetails;
            }
        } catch (UsernameNotFoundException e) {
            // 무시하고 다음 단계로 진행
        }

        try {
            // 호스트 사용자로 조회 시도
            userDetails = hostUserService.loadUserByUsername(username);
            if (userDetails != null) {
                return userDetails;
            }
        } catch (UsernameNotFoundException e) {
            // 무시하고 다음 단계로 진행
        }

        try {
            // 관리자 사용자로 조회 시도
            userDetails = adminUserService.loadUserByUsername(username);
            if (userDetails != null) {
                return userDetails;
            }
        } catch (UsernameNotFoundException e) {
            // 무시하고 예외 처리
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}