package com.springbootfinal.app.controller.myPage;

import com.springbootfinal.app.domain.login.Users;
import com.springbootfinal.app.service.login.HostUserService;
import com.springbootfinal.app.service.login.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class myPageController {

    private static final Logger log = LoggerFactory.getLogger(myPageController.class);

    @Autowired
    HostUserService hostUserService;

    @Autowired
    UserService userService;

    @GetMapping("/myPage/merge")
    public String mergeAccountPage(@AuthenticationPrincipal Object principal, Model model) {
        Users user = null;

        if (principal instanceof OAuth2User) {
            // 소셜 로그인 사용자 처리
            OAuth2User oAuth2User = (OAuth2User) principal;
            String email = oAuth2User.getAttribute("email");
            user = userService.findByEmail(email);
        } else if (principal instanceof UserDetails) {
            // 일반 로그인 사용자 처리
            UserDetails userDetails = (UserDetails) principal;
            String username = userDetails.getUsername();
            user = userService.findById(username);
        }

        model.addAttribute("user", user);
        return "myPage/merge";
    }

    @PostMapping("/myPage/merge")
    public String mergeAccount(@AuthenticationPrincipal Object principal, String localEmail, String localPassword, Model model) {
        Users socialUser = null;
        Users localUser = userService.findByEmail(localEmail);

        if (localUser == null) {
            model.addAttribute("error", "로컬 계정을 찾을 수 없습니다.");
            return "myPage/merge";
        }

        if (principal instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) principal;
            String email = oAuth2User.getAttribute("email");
            socialUser = userService.findByEmail(email);
        } else if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            String username = userDetails.getUsername();
            socialUser = userService.findById(username);
        }

        if (socialUser != null && localUser != null) {
            userService.mergeAccounts(localUser, socialUser);
        }

        return "redirect:/myPage/info";
    }

    @GetMapping("/myPage/info")
    public String userInfoPage(@AuthenticationPrincipal Object principal, Model model) {
        Users user = null;

        if (principal instanceof OAuth2User) {
            // 소셜 로그인 사용자 처리
            OAuth2User oAuth2User = (OAuth2User) principal;
            String email = oAuth2User.getAttribute("email");
            user = userService.findByEmail(email);

            if (user == null) {
                log.info("No user found with email: {}", email);
                throw new UsernameNotFoundException("User not found with email: " + email);
            }

        } else if (principal instanceof UserDetails) {
            // 일반 로그인 사용자 처리
            UserDetails userDetails = (UserDetails) principal;
            String username = userDetails.getUsername(); // 일반 로그인 사용자의 ID를 가져옴
            user = userService.findById(username); // ID로 사용자 정보 조회

            if (user == null) {
                log.info("No user found with ID: {}", username);
                throw new UsernameNotFoundException("User not found with ID: " + username);
            }

        } else {
            // 인증되지 않은 경우 로그인 페이지로 리다이렉트
            log.info("No authenticated user found.");
            return "redirect:/loginPage";
        }

        model.addAttribute("user", user);
        log.info("User info page accessed");
        return "myPage/info"; // 인증된 사용자 정보 페이지 반환
    }

    @GetMapping("/myPage/edit")
    public String showEditForm(@AuthenticationPrincipal Object principal, Model model) {
        Users user = null;

        if (principal instanceof OAuth2User) {
            // 소셜 로그인 사용자 처리
            OAuth2User oAuth2User = (OAuth2User) principal;
            String email = oAuth2User.getAttribute("email");
            user = userService.findByEmail(email);
        } else if (principal instanceof UserDetails) {
            // 일반 로그인 사용자 처리
            UserDetails userDetails = (UserDetails) principal;
            String username = userDetails.getUsername();
            user = userService.findById(username);
        }

        model.addAttribute("user", user);
        return "myPage/edit";
    }

    @PostMapping("/myPage/update")
    public String updateUser(@ModelAttribute Users user, @AuthenticationPrincipal Object principal, RedirectAttributes redirectAttributes) {
        Users currentUser = null;

        // 현재 로그인된 사용자 정보를 가져옴
        if (principal instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) principal;
            String email = oAuth2User.getAttribute("email");
            currentUser = userService.findByEmail(email);
        } else if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            String username = userDetails.getUsername();
            currentUser = userService.findById(username);
        }

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "사용자 정보를 찾을 수 없습니다.");
            return "redirect:/myPage/edit";
        }

        // 비밀번호 필드가 비어 있는 경우, 기존 비밀번호를 유지
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(currentUser.getPassword());
        } else {
            // 새로운 비밀번호를 암호화하여 설정
            user.setPassword(userService.encodePassword(user.getPassword()));
        }

        // 현재 사용자 정보를 기반으로 userNo 설정
        user.setUserNo(currentUser.getUserNo());

        // 디버깅 로그 추가
        log.debug("Updating user: {}", user);

        // 사용자 정보 업데이트
        userService.updateUser(user);
        redirectAttributes.addFlashAttribute("message", "회원 정보가 성공적으로 업데이트되었습니다.");
        return "redirect:/myPage/info";
    }

    @GetMapping("/myPage/myReservationStatus")
    public String myPageReservationStatus() {
        return "myPage/myReservationStatus";
    }
}