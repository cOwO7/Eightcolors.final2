package com.springbootfinal.app.mapper.admin;

import com.springbootfinal.app.domain.admin.RecentOrderDto;
import com.springbootfinal.app.domain.login.HostUser;
import com.springbootfinal.app.domain.login.Users;
import com.springbootfinal.app.domain.residence.ResidenceDto;
import com.springbootfinal.app.domain.transfer.TransferDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminMapper {
    List<RecentOrderDto> findRecentOrders();
    List<ResidenceDto> findAllResidences();
    void deleteRoomByResidNo(Long residNo);

    List<Users> findAllUsers();

    List<Users> searchUsers(String keyword);

    int deleteUserById(@Param("userId") String userId);

    List<HostUser> findAllHostUsers();

    List<HostUser> searchHostUsers(String keyword);

    void deleteHostUserByNos(@Param("hostUserNo") Long hostUserNo);

    void deleteReservationsByHostUserNos(@Param("hostUserNo") Long hostUserNo);

    List<TransferDto> findAllTransfers();

    int deleteTransferById(@Param("transferNo") Long transferNo);

    List<TransferDto> getTransferList(@Param("status") String status,
                                      @Param("keyword") String keyword);

    List<Map<String, Object>> getRoomVacancyRates();
}