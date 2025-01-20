package com.springbootfinal.app.domain.admin;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class adminDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name = "admin_users")
    public static class AdminUser {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long adminUserNo;

        private String adminId;

        private String adminPasswd;

        private String adminName;

        private String role;
    }
}
