package com.springbootfinal.app.domain.login;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admin_users")
public class AdminUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminUserNo; // 관리자 번호
    private String adminUserId; // 관리자 계정명
    private String adminPasswd; // 비밀번호(암호화 저장)
    private String adminUserName; // 관리자 이름

}
