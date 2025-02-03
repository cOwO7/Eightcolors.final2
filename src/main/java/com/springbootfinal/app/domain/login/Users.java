package com.springbootfinal.app.domain.login;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users") // 테이블 이름 명시
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no") // 기본 키 생성을 위한 전략 지정
    private Long userNo;

    private String id;

    private String password;

    private String email;

    private String phone;
    private String name;
    private String zipcode;
    private String address1;
    private String address2;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    private String providerId;

    private LocalDateTime regDate = LocalDateTime.now();

    private int point;



    @Column(nullable = false)
    private String Role;
}