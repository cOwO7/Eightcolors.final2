package com.springbootfinal.app.controller.login;

import com.springbootfinal.app.domain.login.AdminUser;
import com.springbootfinal.app.service.login.AdminUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminUserController {
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private HttpSession session;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 새로운 관리자 계정을 생성합니다.
     *
     * @param adminUser 생성할 관리자 계정 정보
     * @return 생성된 관리자 계정 정보와 상태 코드 201(CREATED)
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<AdminUser> createAdminUser(@RequestBody AdminUser adminUser) {
        return new ResponseEntity<>(adminUserService.createAdminUser(adminUser), HttpStatus.CREATED);
    }

    /**
     * 모든 관리자 계정을 조회합니다.
     *
     * @return 모든 관리자 계정의 리스트
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<AdminUser> getAllAdminUsers() {
        return adminUserService.getAllAdminUsers();
    }

    /**
     * ID를 통해 특정 관리자 계정을 조회합니다.
     *
     * @param id 조회할 관리자 계정의 ID
     * @return 조회된 관리자 계정 정보와 상태 코드 200(OK) 또는 404(NOT FOUND)
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<AdminUser> getAdminUserById(@PathVariable Long id) {
        return adminUserService.getAdminUserById(id)
                .map(adminUser -> new ResponseEntity<>(adminUser, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 기존 관리자 계정을 수정합니다.
     *
     * @param id 수정할 관리자 계정의 ID
     * @param adminUser 수정할 관리자 계정 정보
     * @return 수정된 관리자 계정 정보와 상태 코드 200(OK)
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<AdminUser> updateAdminUser(@PathVariable Long id, @RequestBody AdminUser adminUser) {
        adminUser.setAdminUserNo(id);
        return new ResponseEntity<>(adminUserService.updateAdminUser(adminUser), HttpStatus.OK);
    }

    /**
     * ID를 통해 관리자 계정을 삭제합니다.
     *
     * @param id 삭제할 관리자 계정의 ID
     * @return 상태 코드 204(NO CONTENT)
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdminUser(@PathVariable Long id) {
        adminUserService.deleteAdminUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 로그인 후 세션에 관리자 이름을 저장합니다.
     * 성중 추가
     * @param adminId 로그인할 관리자 계정의 ID
     * @return 로그인 후 리디렉션할 페이지
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String adminId, @RequestParam String password) {
        AdminUser adminUser = (AdminUser) adminUserService.loadUserByUsername(adminId);
        if (adminUser != null && passwordEncoder.matches(password, adminUser.getAdminPasswd())) {
            // 세션에 관리자 이름을 설정
            session.setAttribute("adminName", adminUser.getAdminName());
            return new ResponseEntity<>("로그인 성공", HttpStatus.OK);
        }
        return new ResponseEntity<>("아이디 또는 비밀번호가 잘못되었습니다.", HttpStatus.UNAUTHORIZED);
    }

}