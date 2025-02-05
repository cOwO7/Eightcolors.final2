package com.springbootfinal.app.controller.myPage;

import com.springbootfinal.app.domain.mypage.MyReservation;
import com.springbootfinal.app.domain.login.Users;
import com.springbootfinal.app.service.mypage.MyPageService;
import com.springbootfinal.app.service.login.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class MyPageController {

    private static final Logger log = LoggerFactory.getLogger(MyPageController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private MyPageService myPageService;

    // 사용자 정보를 인증된 principal 객체에서 가져오는 공통 메서드
    private Users getAuthenticatedUser(Object principal) {
        Users user = null;
        if (principal instanceof OAuth2User) {
            String email = ((OAuth2User) principal).getAttribute("email");
            user = userService.findByEmail(email);
        } else if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            user = userService.findById(username);
        }
        return user;
    }

    @GetMapping("/myPage/merge")
    public String mergeAccountPage(@AuthenticationPrincipal Object principal, Model model) {
        Users user = getAuthenticatedUser(principal);

        model.addAttribute("user", user);
        return "myPage/merge";
    }

    @PostMapping("/myPage/merge")
    public String mergeAccount(@AuthenticationPrincipal Object principal, String localEmail, String localPassword, Model model) {
        Users socialUser = getAuthenticatedUser(principal);
        Users localUser = userService.findByEmail(localEmail);

        if (localUser == null) {
            model.addAttribute("error", "로컬 계정을 찾을 수 없습니다.");
            return "myPage/merge";
        }

        if (socialUser != null && localUser != null) {
            userService.mergeAccounts(localUser, socialUser);
        }

        return "redirect:/myPage/info";
    }

    @GetMapping("/myPage/info")
    public String userInfoPage(@AuthenticationPrincipal Object principal, Model model) {
        Users user = getAuthenticatedUser(principal);

        if (user == null) {
            log.info("No authenticated user found.");
            return "redirect:/loginPage";
        }

        model.addAttribute("user", user);
        log.info("User info page accessed");
        return "myPage/info";
    }

    @GetMapping("/myPage/edit")
    public String showEditForm(@AuthenticationPrincipal Object principal, Model model) {
        Users user = getAuthenticatedUser(principal);

        model.addAttribute("user", user);
        return "myPage/edit";
    }

    @PostMapping("/myPage/update")
    public String updateUser(@ModelAttribute Users user, @AuthenticationPrincipal Object principal, RedirectAttributes redirectAttributes) {
        Users currentUser = getAuthenticatedUser(principal);

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

    @GetMapping("/myPage/myReservation")
    public String myReservations(@AuthenticationPrincipal Object principal, Model model) {
        Users user = getAuthenticatedUser(principal);

        if (user == null) {
            log.info("No authenticated user found.");
            return "redirect:/loginPage";
        }

        List<MyReservation> reservations = myPageService.getMyReservationsByUserNo(user.getUserNo());
        model.addAttribute("reservations", reservations);
        return "myPage/myReservation";
    }

    @GetMapping("/myPage/myReservationDetail/{reservationNo}")
    public String myReservationDetail(@PathVariable Long reservationNo, Model model) {
        MyReservation reservation = myPageService.getReservationById(reservationNo);

        if (reservation == null) {
            log.info("No reservation found with ID: {}", reservationNo);
            return "redirect:/myPage/myReservation";
        }

        model.addAttribute("reservation", reservation);
        return "myPage/myReservationDetail";
    }
}