package com.springbootfinal.app.domain.login;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "host_users")
@Getter
@Setter
public class HostUser {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "host_user_no")
    private Long hostUserNo;

    @Column(unique = true, nullable = false)
    private String id;

    @Column(nullable = false)
    private String passwd;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String zipcode;

    @Column(nullable = false)
    private String address1;

    @Column(nullable = false)
    private String address2;

    @Column(nullable = false)
    private String businessLicenseNo;

    @Column
    private LocalDateTime regdate;

    @Column
    private String role = "ROLE_HOST";

    // Getters and Setters
}