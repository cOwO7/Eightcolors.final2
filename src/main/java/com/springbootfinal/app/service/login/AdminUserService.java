package com.springbootfinal.app.service.login;

import com.springbootfinal.app.domain.login.AdminUser;
import com.springbootfinal.app.mapper.login.AdminUserMapper;
import jakarta.servlet.http.HttpSession;
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
public class AdminUserService implements UserDetailsService {

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HttpSession session;

    /**
     * 관리자 계정을 생성합니다.
     * @param adminUser 생성할 관리자 계정 정보
     * @return 생성된 관리자 계정 정보
     */
    public AdminUser createAdminUser(AdminUser adminUser) {
        adminUser.setAdminPasswd(passwordEncoder.encode(adminUser.getAdminPasswd()));
        adminUser.setRole("ROLE_ADMIN");
        adminUserMapper.insertAdminUser(adminUser);
        return adminUser;
    }

    /**
     * 모든 관리자 계정을 조회합니다.
     * @return 모든 관리자 계정의 리스트
     */
    public List<AdminUser> getAllAdminUsers() {
        return adminUserMapper.selectAllAdminUsers();
    }

    /**
     * 관리자 ID를 통해 특정 관리자 계정을 조회합니다.
     * @param id 조회할 관리자 계정의 ID
     * @return 조회된 관리자 계정 정보
     */
    public Optional<AdminUser> getAdminUserById(Long id) {
        return adminUserMapper.selectAdminUserById(id);
    }

    /**
     * 관리자 정보를 수정합니다.
     * @param adminUser 수정할 관리자 계정 정보
     * @return 수정된 관리자 계정 정보
     */
    public AdminUser updateAdminUser(AdminUser adminUser) {
        // 비밀번호가 변경된 경우 암호화
        if (adminUser.getAdminPasswd() != null) {
            adminUser.setAdminPasswd(passwordEncoder.encode(adminUser.getAdminPasswd()));
        }
        adminUserMapper.updateAdminUser(adminUser);
        return adminUser;
    }

    /**
     * 관리자 ID를 통해 특정 관리자 계정을 삭제합니다.
     * @param id 삭제할 관리자 계정의 ID
     */
    public void deleteAdminUser(Long id) {
        adminUserMapper.deleteAdminUser(id);
    }

    /**
     * 관리자 ID를 통해 특정 관리자 계정을 조회합니다.
     * @param adminId 조회할 관리자 계정의 ID
     * @return 조회된 관리자 계정 정보
     */
    public Optional<AdminUser> findAdminUserByAdminId(String adminId) {
        return adminUserMapper.selectAdminUserByAdminId(adminId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser adminUser = adminUserMapper.selectAdminUserByAdminId(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(adminUser.getRole()));

        // 세션에 관리자 이름을 추가 (이부분 성중 추가)
        session.setAttribute("adminName", adminUser.getAdminName());

        return new org.springframework.security.core.userdetails.User(adminUser.getAdminId(), adminUser.getAdminPasswd(), authorities);
    }
}