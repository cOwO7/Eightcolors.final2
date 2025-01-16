package com.springbootfinal.app.mapper.transfer;

import com.springbootfinal.app.domain.transfer.TransferDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TransferMapper {

    //게시글을 transferDto 객체로 받아서 DB 테이블에 추가하는 메서드
    void transferInsert(TransferDto transfer);

    TransferDto transferRead(int no);
    List<TransferDto> transferList();
}
