package com.springbootfinal.app.mapper.transfer;

import com.springbootfinal.app.domain.transfer.TransferDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TransferMapper {

    public void transferUpdate(TransferDto transferDto);

    // 한 페이지에 해당하는 게시글 리스트를 DB 테이블에서 읽어와 반환하는 메서드
    List<TransferDto> transferList(@Param("startRow") int startRow, @Param("pageSize") int pageSize,
                                   @Param("searchType") String searchType, @Param("searchKeyword") String searchKeyword);

    // DB 테이블에 등록된 전체 게시글 수를 읽어와 반환하는 메서드
    int transferCount(@Param("searchType") String searchType, @Param("searchKeyword") String searchKeyword);

    // 게시글을 transferDto 객체로 받아서 DB 테이블에 추가하는 메서드
    void transferInsert(TransferDto transfer);

    // 특정 게시글을 읽어오는 메서드
    TransferDto transferRead(Long no);

    // 전체 게시글 리스트를 반환하는 메서드
    List<TransferDto> transferList();
}