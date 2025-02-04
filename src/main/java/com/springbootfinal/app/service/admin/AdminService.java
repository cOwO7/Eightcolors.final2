package com.springbootfinal.app.service.admin;

import com.springbootfinal.app.domain.admin.RecentOrderDto;
import com.springbootfinal.app.domain.login.HostUser;
import com.springbootfinal.app.domain.login.Users;
import com.springbootfinal.app.domain.residence.ResidenceDto;
import com.springbootfinal.app.domain.transfer.TransferDto;
import com.springbootfinal.app.mapper.admin.AdminMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminMapper adminMapper;
    private final EntityManager entityManager;

    public List<Users> getAllUsers() {
        return entityManager.createQuery("SELECT u FROM Users u", Users.class).getResultList();
    }

    public List<Users> getUserList(String keyword) {
        return adminMapper.searchUsers(keyword);
    }

    @Transactional
    public boolean deleteUserById(String userId) {
        try {
            int affectedRows = adminMapper.deleteUserById(userId);

            return affectedRows > 0;
        } catch (Exception e) {
            throw new RuntimeException("삭제 실패: " + e.getMessage(), e);
        }
    }

    public List<HostUser> getAllHostUsers() {
        return entityManager.createQuery("SELECT h FROM HostUser h", HostUser.class).getResultList();
    }

    public List<HostUser> getHostUserList(String keyword) {
        return adminMapper.searchHostUsers(keyword);
    }

    @Transactional
    public boolean deleteHostUserByNos(Long hostUserNo) {
        try {
            adminMapper.deleteReservationsByHostUserNos(hostUserNo);
            adminMapper.deleteHostUserByNos(hostUserNo);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("삭제 실패: " + e.getMessage(), e);
        }
    }

    /**
     * 공실 갯수 구하는 메서드
     */
    public List<Map<String, Object>> getRoomVacancyRates() {
        return adminMapper.getRoomVacancyRates();
    }

    public List<RecentOrderDto> getRecentReservations() {
        List<RecentOrderDto> recentOrders = adminMapper.findRecentOrders();
        if(recentOrders != null && !recentOrders.isEmpty()) {
            System.out.println("Recent Orders: " + recentOrders);
        } else {
            System.out.println("No recent orders found.");
        }
        return recentOrders;
    }

    /*public List<ResidenceDto> getAllResidences() {
        return adminMapper.findAllResidences();
    }*/


    public List<TransferDto> getAllTransfers() {
        return adminMapper.findAllTransfers();
    }

    public List<TransferDto> getTransferList(String status, String keyword) {
        return adminMapper.getTransferList(status, keyword);
    }

    @Transactional
    public boolean deleteTransferById(Long transferNo) {
        try {
            int affectedRows = adminMapper.deleteTransferById(transferNo);

            return affectedRows > 0;
        } catch (Exception e) {
            throw new RuntimeException("양도 삭제 실패: " + e.getMessage(), e);
        }
    }

    @Transactional
    public List<ResidenceDto> findResidences(String residType, String keyword) {
        return adminMapper.findResidences(residType, keyword);
    }

    public void deleteRoomByResidNo(Long residNo) {
        adminMapper.deleteRoomByResidNo(residNo);
    }

}