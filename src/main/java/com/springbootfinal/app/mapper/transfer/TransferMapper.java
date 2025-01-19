package com.springbootfinal.app.mapper.transfer;

import com.springbootfinal.app.domain.transfer.TransferDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface TransferMapper {

    // 한 페이지에 출력할 게시글 리스트 또는 검색 리스트를 게시판 테이블에서 읽어와 반환하는 메서드
    List<TransferDto> transferList(
            @Param("startRow") int startRow, @Param("count") int count,
            @Param("search") String search, @Param("searchText") String searchText);

    // DB 테이블에 등록된 전체 게시글 수를 읽어와 반환하는 메서드
    int transferCount(
            @Param("search") String search, @Param("searchText") String searchText);

    //한 페이지에 해당하는 게시글 리스트를 DB 테이블에서 읽어와 반환하는 메서드
    List<TransferDto> transferList(
            @Param("startRow") int startRow, @Param("count") int count);

    int getTransferCount();

    //게시글을 transferDto 객체로 받아서 DB 테이블에 추가하는 메서드
    void transferInsert(TransferDto transfer);

    TransferDto transferRead(Long no);
    List<TransferDto> transferList();
}
