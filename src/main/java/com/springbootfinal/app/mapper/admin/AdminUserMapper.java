package com.springbootfinal.app.mapper.admin;

import com.springbootfinal.app.domain.admin.adminDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AdminUserMapper {

    void insertAdminUser(adminDTO.AdminUser adminUser);

    List<adminDTO.AdminUser> selectAllAdminUsers();

    Optional<adminDTO.AdminUser> selectAdminUserById(Long id);

    void updateAdminUser(adminDTO.AdminUser adminUser);

    void deleteAdminUser(Long id);

    Optional<adminDTO.AdminUser> selectAdminUserByAdminId(String adminId);
}