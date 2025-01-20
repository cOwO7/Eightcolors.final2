package com.springbootfinal.app.controller.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminPageController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminDashboard() {
        return "admin/admin"; // admin/dashboard.html 뷰를 반환
    }

    @GetMapping("/settings")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminSettings() {
        return "admin/settings"; // admin/settings.html 뷰를 반환
    }
}