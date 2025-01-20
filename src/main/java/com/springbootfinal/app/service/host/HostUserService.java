package com.springbootfinal.app.service.host;

import com.springbootfinal.app.domain.host.HostUserDTO;
import com.springbootfinal.app.mapper.host.HostUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
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
    public HostUserDTO createHostUser(HostUserDTO hostUser) {
        hostUser.setPasswd(passwordEncoder.encode(hostUser.getPasswd()));
        hostUser.setRole("ROLE_HOST");
        hostUserMapper.insertHostUser(hostUser);
        return hostUser;
    }

    /**
     * 모든 호스트 사용자를 조회합니다.
     * @return 모든 호스트 사용자의 리스트
     */
    public List<HostUserDTO> getAllHostUsers() {
        return hostUserMapper.findAllHostUsers();
    }

    /**
     * 호스트 사용자 ID를 통해 특정 호스트 사용자를 조회합니다.
     * @param id 조회할 호스트 사용자의 ID
     * @return 조회된 호스트 사용자 정보
     */
    public Optional<HostUserDTO> getHostUserById(Long id) {
        return Optional.ofNullable(hostUserMapper.findHostUserByHostUserNo(id));
    }

    /**
     * 호스트 사용자 정보를 수정합니다.
     * @param hostUser 수정할 호스트 사용자 정보
     * @return 수정된 호스트 사용자 정보
     */
    public HostUserDTO updateHostUserById(HostUserDTO hostUser) {
        // 비밀번호가 변경된 경우 암호화
        if (hostUser.getPasswd() != null) {
            hostUser.setPasswd(passwordEncoder.encode(hostUser.getPasswd()));
        }
        hostUserMapper.updateHostUserById(hostUser);
        return hostUser;
    }

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
    public HostUserDTO findHostUserById(String id) {
        return hostUserMapper.findHostUserById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HostUserDTO hostUser = hostUserMapper.findHostUserById(username);
        if (hostUser == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(hostUser.getRole()));

        return new org.springframework.security.core.userdetails.User(
                hostUser.getId(),
                hostUser.getPasswd(),
                authorities
        );
    }
}