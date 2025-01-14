package com.springbootfinal.app.mapper.transfer;

import com.springbootfinal.app.domain.transfer.TransferDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TransferMapper {

    TransferDto transferRead(int no);
    List<TransferDto> transferList();
}
