package com.springbootfinal.app.mapper.transfer;

import com.springbootfinal.app.domain.transfer.TransferDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TransferMapper {

    /**
     * Transfer 테이블에서 모든 데이터를 조회하여 TransferDto 리스트로 반환
     *
     * @return TransferDto 리스트
     */
    List<TransferDto> transferList();
}
