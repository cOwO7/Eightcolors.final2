package com.springbootfinal.app.mapper.login;

import com.springbootfinal.app.domain.login.AdminUser;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AdminUserMapper {

    void insertAdminUser(AdminUser adminUser);

    List<AdminUser> selectAllAdminUsers();

    Optional<AdminUser> selectAdminUserById(Long id);

    void updateAdminUser(AdminUser adminUser);

    void deleteAdminUser(Long id);

    Optional<AdminUser> selectAdminUserByAdminId(String adminId);
}