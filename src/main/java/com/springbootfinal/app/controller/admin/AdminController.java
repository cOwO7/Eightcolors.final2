package com.springbootfinal.app.controller.admin;

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

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminDashboard(Model model) {
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