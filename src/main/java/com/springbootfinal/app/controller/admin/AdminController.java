package com.springbootfinal.app.controller.admin;

import com.springbootfinal.app.domain.login.HostUser;
import com.springbootfinal.app.domain.login.Users;
import com.springbootfinal.app.service.login.HostUserService;
import com.springbootfinal.app.service.login.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import com.springbootfinal.app.service.admin.AdminService;
import com.springbootfinal.app.domain.admin.RecentOrderDto;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;

    @Autowired
    private HostUserService HostUserService;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminDashboard(Model model) {

        //최근 가입한 호스트 목록
        List<HostUser> HostuserList = HostUserService.findRecentHostUsers(4);
        model.addAttribute("HostUserList", HostuserList);

        //최근 가입한 사용자 목록
        List<Users> userList = userService.getRecentUsers(4);
        model.addAttribute("userList", userList);

        //최근 결제 목록
        List<RecentOrderDto> recentOrders = adminService.getRecentReservations();
        model.addAttribute("orderList", recentOrders);
        return "admin/admin"; // admin/admin.html 뷰를 반환
    }

    @GetMapping("/settings")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminSettings() {
        return "admin/settings"; // admin/settings.html 뷰를 반환
    }
}