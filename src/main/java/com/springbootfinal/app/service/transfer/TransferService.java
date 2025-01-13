package com.springbootfinal.app.service.transfer;

import com.springbootfinal.app.domain.transfer.TransferDto;
import com.springbootfinal.app.mapper.transfer.TransferMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TransferService {

    private final TransferMapper transferMapper;

    // 생성자 주입 방식
    public TransferService(TransferMapper transferMapper) {
        this.transferMapper = transferMapper;
    }

    // 데이터 리스트를 가져오는 메소드
    public List<TransferDto> getTransferList() {
        log.info("Fetching transfer list...");
        List<TransferDto> transferList = transferMapper.transferList();
        log.info("Fetched {} transfer records.", transferList.size());
        return transferList;
    }
}
