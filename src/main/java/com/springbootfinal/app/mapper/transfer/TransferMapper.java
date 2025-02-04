package com.springbootfinal.app.mapper.transfer;

import com.springbootfinal.app.domain.transfer.TransferDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TransferMapper {

    @Select("SELECT COUNT(*) FROM transfers WHERE reservation_no = #{reservationNo}")
    int countByReservationNo(Long reservationNo);

    // 게시글 번호에 해당하는 게시글을 읽어오는 메서드
    @Select("SELECT * FROM transfers WHERE partner_order_id = #{partnerOrderId}")
    TransferDto findByPartnerOrderId(@Param("partnerOrderId") String partnerOrderId);

    // 게시글 번호에 해당하는 게시글의 조회수를 증가시키는 메서드
    @Update("UPDATE transfers SET status = #{status} WHERE partner_order_id = #{partnerOrderId}")
    void updateTransferStatus(@Param("partnerOrderId") String partnerOrderId, @Param("status") String status);

    // no에 해당하는 게시글의 읽은 횟수를 DB 테이블에서 증가시키는 메서드
    public void incrementReadCount(Long transferNo);

    // no에 해당하는 게시글을 DB 테이블에서 삭제하는 메서드
    public void deleteTransfer(Long transferNo);

    // 게시글을 DB 테이블에 추가하는 메서드
    public void putTransfer(TransferDto transferDto);

    // 게시글을 수정하는 메서드
    public void updateBoard(TransferDto transferDto);

    // 한 페이지에 해당하는 게시글 리스트를 DB 테이블에서 읽어와 반환하는 메서드
    List<TransferDto> transferList(@Param("startRow") int startRow, @Param("num") int num,
                                   @Param("type") String type, @Param("keyword") String keyword);

    // DB 테이블에 등록된 전체 게시글 수를 읽어와 반환하는 메서드
    int getTransferCount(@Param("type") String type, @Param("keyword") String keyword);

    // 게시글을 transferDto 객체로 받아서 DB 테이블에 추가하는 메서드
    void transferInsert(TransferDto transfer);

    // 특정 게시글을 읽어오는 메서드
    TransferDto transferRead(Long transferNo, boolean isCount);

    // 전체 게시글 리스트를 반환하는 메서드
    List<TransferDto> transferList();

}