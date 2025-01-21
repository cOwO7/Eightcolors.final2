package com.springbootfinal.app.service.admin;

import com.springbootfinal.app.domain.admin.RecentOrderDto;
import com.springbootfinal.app.mapper.admin.AdminReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminReservationMapper adminReservationMapper;

    public List<RecentOrderDto> getRecentReservations() {
        List<RecentOrderDto> recentOrders = adminReservationMapper.findRecentOrders();
        if(recentOrders != null && !recentOrders.isEmpty()) {
            System.out.println("Recent Orders: " + recentOrders);
        } else {
            System.out.println("No recent orders found.");
        }
        return recentOrders;
    }
}