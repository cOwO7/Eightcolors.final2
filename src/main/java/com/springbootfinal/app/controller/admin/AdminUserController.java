package com.springbootfinal.app.controller.admin;

import com.springbootfinal.app.domain.admin.adminDTO;
import com.springbootfinal.app.service.admin.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminUserController {
    @Autowired
    private AdminUserService adminUserService;

    /**
     * 새로운 관리자 계정을 생성합니다.
     *
     * @param adminUser 생성할 관리자 계정 정보
     * @return 생성된 관리자 계정 정보와 상태 코드 201(CREATED)
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<adminDTO.AdminUser> createAdminUser(@RequestBody adminDTO.AdminUser adminUser) {
        return new ResponseEntity<>(adminUserService.createAdminUser(adminUser), HttpStatus.CREATED);
    }

    /**
     * 모든 관리자 계정을 조회합니다.
     *
     * @return 모든 관리자 계정의 리스트
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<adminDTO.AdminUser> getAllAdminUsers() {
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
    public ResponseEntity<adminDTO.AdminUser> getAdminUserById(@PathVariable Long id) {
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
    public ResponseEntity<adminDTO.AdminUser> updateAdminUser(@PathVariable Long id, @RequestBody adminDTO.AdminUser adminUser) {
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
}