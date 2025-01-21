package com.springbootfinal.app.mapper.login;

import com.springbootfinal.app.domain.login.HostUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HostUserMapper {

    // 특정 ID로 호스트 사용자 조회
    HostUser findHostUserById(@Param("id") String id);

    // 특정 HostUserNo로 호스트 사용자 조회
    HostUser findHostUserByHostUserNo(@Param("hostUserNo") Long hostUserNo);

    // 이름으로 호스트 사용자 조회
    HostUser findHostUserByName(@Param("name") String name);

    // 이메일로 호스트 사용자 조회
    HostUser findHostUserByEmail(@Param("email") String email);

    // 모든 호스트 사용자 조회
    List<HostUser> findAllHostUsers();

    // 호스트 사용자 삽입
    void insertHostUser(HostUser hostUser);

    // 호스트 사용자 정보 업데이트
    void updateHostUserById(HostUser hostUser);

    // 특정 HostUserNo로 호스트 사용자 삭제
    void deleteHostUserById(@Param("hostUserNo") Long hostUserNo);
}