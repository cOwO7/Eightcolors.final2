package com.springbootfinal.app.controller.admin;

import com.springbootfinal.app.domain.login.HostUser;
import com.springbootfinal.app.domain.login.Users;
import com.springbootfinal.app.domain.residence.ResidenceDto;
import com.springbootfinal.app.domain.transfer.TransferDto;
import com.springbootfinal.app.mapper.admin.AdminMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.springbootfinal.app.service.admin.AdminService;
import com.springbootfinal.app.domain.admin.RecentOrderDto;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;
    private final AdminMapper adminMapper;


    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminDashboard(Model model) {
        List<RecentOrderDto> recentOrders = adminService.getRecentReservations();
        model.addAttribute("orderList", recentOrders);
        return "admin/admin";
    }

    @GetMapping("/membercheck")
    public String getUserList(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        if (keyword != null) {
            keyword = keyword.trim();
        }
        List<Users> userList;
        if (keyword == null || keyword.isEmpty()) {
            userList = adminService.getAllUsers();
        } else {
            userList = adminService.getUserList(keyword);
        }
        model.addAttribute("userList", userList);
        return "admin/membercheck";
    }

    @PostMapping("/user/delete")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteUser(@RequestParam("userId") String userId) {
        Map<String, String> response = new HashMap<>();

        try {
            log.info("🔍 삭제 요청 받은 userId: {}", userId);

            if (userId == null || userId.trim().isEmpty()) {
                response.put("status", "error");
                response.put("message", "잘못된 사용자 ID입니다.");
                return ResponseEntity.badRequest().body(response);
            }

            boolean isDeleted = adminService.deleteUserById(userId);
            if (isDeleted) {
                response.put("status", "success");
                response.put("message", "삭제 완료");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "해당 사용자가 존재하지 않습니다.");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            System.err.println("❌ 삭제 중 예외 발생: " + e.getMessage());
            response.put("status", "error");
            response.put("message", "삭제 중 오류 발생: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }


    @GetMapping("/hostcheck")
    public String getHostUserList(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        if (keyword != null) {
            keyword = keyword.trim();
        }
        List<HostUser> hostUserList;
        if (keyword == null || keyword.isEmpty()) {
            hostUserList = adminService.getAllHostUsers();
        } else {
            hostUserList = adminService.getHostUserList(keyword);
        }
        model.addAttribute("hostUserList", hostUserList);
        return "admin/hostcheck";
    }

    @PostMapping("/hostcheck/delete")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteHostUser(@RequestParam("hostUserNo") Long hostUserNo) {
        Map<String, String> response = new HashMap<>();

        log.info("🔍 삭제 요청 받은 hostUserNo: {}", hostUserNo);

        try {
            if (hostUserNo == null) {
                response.put("status", "error");
                response.put("message", "잘못된 호스트 사용자 번호입니다.");
                return ResponseEntity.badRequest().body(response);
            }

            boolean isDeleted = adminService.deleteHostUserByNos(hostUserNo);
            if (isDeleted) {
                response.put("status", "success");
                response.put("message", "호스트 사용자 삭제 완료");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "해당 호스트 사용자가 존재하지 않습니다.");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            log.error("❌ 삭제 중 예외 발생: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "삭제 중 오류 발생: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }



    @GetMapping("/settings")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminSettings() {
        return "admin/settings"; // admin/settings.html 뷰를 반환
    }

    @GetMapping("/transcheck")
    public String getTransferList(@RequestParam(value = "keyword", required = false) String keyword,
                                  @RequestParam(value = "status", required = false) String status,
                                  Model model) {
        if (keyword != null) {
            keyword = keyword.trim();
        }
        List<TransferDto> transList;

        if ((keyword == null || keyword.isEmpty()) && (status == null || status.isEmpty())) {
            transList = adminService.getAllTransfers();
        } else {
            transList = adminService.getTransferList(status, keyword);
        }

        model.addAttribute("transList", transList);
        return "admin/transcheck";
    }

    @PostMapping("/transcheck/delete")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteTransfer(@RequestParam("transferNo") Long transferNo) {
        Map<String, String> response = new HashMap<>();

        try {
            if (transferNo == null) {
                response.put("status", "error");
                response.put("message", "잘못된 양도 번호입니다.");
                return ResponseEntity.badRequest().body(response);
            }

            boolean isDeleted = adminService.deleteTransferById(transferNo);
            if (isDeleted) {
                response.put("status", "success");
                response.put("message", "양도 삭제 완료");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "해당 양도가 존재하지 않습니다.");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            System.err.println("❌ 양도 삭제 중 예외 발생: " + e.getMessage());
            response.put("status", "error");
            response.put("message", "삭제 중 오류 발생: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/roomcheck")
    public String showRoomCheckPage(Model model) {
        List<ResidenceDto> allResidences = adminService.getAllResidences();
        List<HostUser> AllHosts = adminService.getAllHostUsers();
        List<Map<String, Object>> roomVacancyRates = adminService.getRoomVacancyRates();

        for(ResidenceDto residence : allResidences) {
            String hostId = AllHosts.stream()
                    .filter(host -> host.getHostUserNo().equals(residence.getHostUserNo()))
                    .map(HostUser::getId)
                    .findFirst()
                    .orElse("미등록");
            residence.setHostId(hostId);

            for(Map<String, Object> roomVacancy : roomVacancyRates) {
                if(roomVacancy.get("resid_no").equals(residence.getResidNo())) {
                    residence.setTotalRooms((Number) roomVacancy.get("total_rooms"));
                    residence.setOccupiedRooms((Number) roomVacancy.get("occupied_rooms"));
                    residence.setAvailableRooms((Number) roomVacancy.get("available_rooms"));
                    residence.setVacancyRate((Number) roomVacancy.get("vacancy_rate"));
                }
            }
        }

        model.addAttribute("residences", allResidences);
        model.addAttribute("hosts", AllHosts);
        return "admin/roomcheck";
    }

    @GetMapping("/roomcheck/{residNo}")
    public String test(@PathVariable Long residNo) {
        log.info("residNo: {}", residNo);
        adminMapper.deleteRoomByResidNo(residNo);
        return "/admin/roomcheck";
    }




    /*@GetMapping("/roomcheck")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showRoomCheckPage(Model model) {
        List<ResidenceDto> residenceList = adminService.getAllResidences();
        if (residenceList == null || residenceList.isEmpty()) {
            System.out.println("No residences found.");
        } else {
            System.out.println("Found " + residenceList.size() + " residences.");
        }
        model.addAttribute("residenceList", residenceList);
        return "admin/roomcheck";
    }*/

}