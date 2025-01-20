package com.springbootfinal.app.mapper.admin;

import com.springbootfinal.app.domain.admin.AdminUserDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AdminUserMapper {

    void insertAdminUser(AdminUserDTO.AdminUser adminUser);

    List<AdminUserDTO.AdminUser> selectAllAdminUsers();

    Optional<AdminUserDTO.AdminUser> selectAdminUserById(Long id);

    void updateAdminUser(AdminUserDTO.AdminUser adminUser);

    void deleteAdminUser(Long id);

    Optional<AdminUserDTO.AdminUser> selectAdminUserByAdminId(String adminId);
}