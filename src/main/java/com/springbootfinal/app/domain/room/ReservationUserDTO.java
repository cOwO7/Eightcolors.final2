package com.springbootfinal.app.domain.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationUserDTO {

    private Long userNo;          // 사용자 고유 번호
    private String id;            // 사용자 아이디
    private String password;      // 비밀번호
    private String email;         // 이메일
    private String phone;         // 전화번호
    private String name;          // 이름
    private String zipcode;       // 우편번호
    private String address1;      // 기본 주소
    private String address2;      // 상세 주소
    private String loginType;     // 로그인 타입 (예: 일반, 소셜 등)
    private String providerId;    // 제공자 ID (소셜 로그인 제공자)
    private LocalDateTime regDate;// 등록일자
    private String role;          // 사용자 권한



}
