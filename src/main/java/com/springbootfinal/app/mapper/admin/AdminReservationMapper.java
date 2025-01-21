package com.springbootfinal.app.mapper.admin;

import com.springbootfinal.app.domain.admin.RecentOrderDto;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AdminReservationMapper {
    List<RecentOrderDto> findRecentOrders();
}