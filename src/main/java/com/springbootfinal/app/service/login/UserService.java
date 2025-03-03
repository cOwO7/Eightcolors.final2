package com.springbootfinal.app.service.login;

import com.springbootfinal.app.domain.login.LoginType;
import com.springbootfinal.app.domain.login.Users;
import com.springbootfinal.app.domain.residence.login.RegisterRequest;
import com.springbootfinal.app.mapper.login.AdminUserMapper;
import com.springbootfinal.app.mapper.login.HostUserMapper;
import com.springbootfinal.app.mapper.login.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private HostUserMapper hostUserMapper;
    @Autowired
    private AdminUserMapper adminUserMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private Users currentUser; // 현재 사용자 정보를 보관

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // 비밀번호 암호화 메서드
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    // 로컬 계정 정보를 소셜 계정으로 병합
    @Transactional
    public void mergeAccounts(Users localUser, Users socialUser) {
        socialUser.setPassword(localUser.getPassword());
        socialUser.setPhone(localUser.getPhone());
        socialUser.setZipcode(localUser.getZipcode());
        socialUser.setAddress1(localUser.getAddress1());
        socialUser.setAddress2(localUser.getAddress2());
        userMapper.updateUser(socialUser);
        userMapper.deleteUser(localUser.getUserNo()); // 로컬 계정 삭제
    }

    // 아이디 중복 확인 메서드
    public boolean overlapIdCheck(String id) {
        return userMapper.findById(id) != null ||
                hostUserMapper.findHostUserById(id) != null ||
                adminUserMapper.selectAdminUserByAdminId(id).isPresent();
    }

    // 사용자 저장 메서드
    @Transactional
    public void insertUser(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // 비밀번호 암호화
        userMapper.insertUser(user);
    }

    // 로컬 로그인 사용자 저장
    @Transactional
    public void registerUser(RegisterRequest request) {
        Users user = new Users();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setLoginType(LoginType.LOCAL);
        user.setRole("ROLE_USER");
        userMapper.insertUser(user);
    }

    // 소셜 로그인 사용자 저장
    @Transactional
    public Users saveSocialUser(String email, String name, String providerId, LoginType loginType) {
        Users user = new Users();
        user.setEmail(email);
        user.setName(name);
        user.setProviderId(providerId);
        user.setLoginType(loginType);
        user.setRole("ROLE_USER");
        userMapper.insertUser(user);
        return user;
    }

    // 사용자 정보 업데이트 메서드 추가
    @Transactional
    public void updateUser(Users user) {
        log.debug("Updating user: {}", user);
        userMapper.updateUser(user);
    }

    // 회원 ID에 해당하는 회원 정보를 읽어와 반환하는 메서드
    public Users findById(String id) {
        log.info("Finding user by ID: {}", id); // ID 로그 출력
        Users user = userMapper.findById(id);
        if (user != null) {
            log.info("User found: {}", user);
        } else {
            log.warn("No user found with ID: {}", id);
        }
        return user;
    }

    // 회원 번호로 회원을 찾는 메서드
    public Users findByUserNo(Long userNo) {
        log.info("Finding user by userNo: {}", userNo); // userNo 로그 출력
        Users user = userMapper.findByUserNo(userNo);
        if (user != null) {
            log.info("User found: {}", user);
        } else {
            log.warn("No user found with userNo: " + userNo);
        }
        return user;
    }

    // 회원 이름으로 회원을 찾는 메서드
    public Users findByName(String name) {
        return userMapper.findByName(name);
    }

    // 이메일로 회원을 찾는 메서드
    public Users findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        currentUser = userMapper.findById(username);
        if (currentUser == null) {
            log.error("User not found with username: " + username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        log.info("User found with username: " + username);
        return new org.springframework.security.core.userdetails.User(
                currentUser.getId(),
                currentUser.getPassword(),
                AuthorityUtils.createAuthorityList(currentUser.getRole())
        );
    }

    // 현재 로그인된 사용자 정보 가져오기
    public Users getCurrentUser() {
        return currentUser;
    }

    // 성중 추가 문제시 삭제
    public String getUserNameByUserNo(Long userNo) {
        Users user = findByUserNo(userNo);  // userNo로 사용자 찾기
        if (user != null) {
            return user.getName();  // 해당 사용자의 name 반환
        }
        return null;  // 사용자 없으면 null 반환
    }
}