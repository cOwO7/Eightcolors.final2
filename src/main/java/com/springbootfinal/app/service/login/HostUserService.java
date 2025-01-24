package com.springbootfinal.app.service.login;

import com.springbootfinal.app.domain.login.HostUser;
import com.springbootfinal.app.mapper.login.HostUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class HostUserService implements UserDetailsService {

    @Autowired
    private HostUserMapper hostUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 호스트 사용자를 생성합니다.
     * @param hostUser 생성할 호스트 사용자 정보
     * @return 생성된 호스트 사용자 정보
     */
    public HostUser createHostUser(HostUser hostUser) {
        hostUser.setPasswd(passwordEncoder.encode(hostUser.getPasswd()));
        hostUser.setRole("ROLE_HOST");
        hostUserMapper.insertHostUser(hostUser);
        return hostUser;
    }

    /**
     * 모든 호스트 사용자를 조회합니다.
     * @return 모든 호스트 사용자의 리스트
     */
    public List<HostUser> getAllHostUsers() {
        return hostUserMapper.findAllHostUsers();
    }

    /**
     * 호스트 사용자 ID를 통해 특정 호스트 사용자를 조회합니다.
     * @param id 조회할 호스트 사용자의 ID
     * @return 조회된 호스트 사용자 정보
     */
    public Optional<HostUser> getHostUserById(Long id) {
        return Optional.ofNullable(hostUserMapper.findHostUserByHostUserNo(id));
    }

    /**
     * 호스트 사용자 정보를 수정합니다.
     * @param hostUser 수정할 호스트 사용자 정보
     * @return 수정된 호스트 사용자 정보
     */
    public HostUser updateHostUserById(HostUser hostUser) {
        // 비밀번호가 변경된 경우 암호화
        if (hostUser.getPasswd() != null) {
            hostUser.setPasswd(passwordEncoder.encode(hostUser.getPasswd()));
        }
        hostUserMapper.updateHostUserById(hostUser);
        return hostUser;
    }

    /**
     * 호스트 사용자 이름을 통해 특정 호스트 사용자를 조회합니다.
     * @param username 조회할 호스트 사용자의 이름
     * @return 조회된 호스트 사용자 정보
     */


    /**
     * 호스트 사용자 ID를 통해 특정 호스트 사용자를 삭제합니다.
     * @param id 삭제할 호스트 사용자의 ID
     */
    public void deleteHostUserById(Long id) {
        hostUserMapper.deleteHostUserById(id);
    }

    /**
     * 호스트 사용자 ID를 통해 특정 호스트 사용자를 조회합니다.
     * @param id 조회할 호스트 사용자의 ID
     * @return 조회된 호스트 사용자 정보
     */
    public HostUser findHostUserById(String id) {
        HostUser hostUser = hostUserMapper.findHostUserById(id);
        log.info("HostUser found: {}", hostUser); // 로그 추가
        return hostUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HostUser hostUser = hostUserMapper.findHostUserById(username);
        if (hostUser == null) {
            log.error("User not found with username: " + username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        log.info("HostUser found for loadUserByUsername: {}", hostUser); // 로그 추가

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(hostUser.getRole()));

        return new org.springframework.security.core.userdetails.User(
                hostUser.getId(),
                hostUser.getPasswd(),
                authorities
        );
    }

    /**
     * 현재 인증된 호스트 사용자의 정보를 반환합니다.
     * @return 현재 인증된 호스트 사용자 정보
     */
    public HostUser getCurrentHostUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return hostUserMapper.findHostUserById(username);
    }
}