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

    // 게시글 정보를 추가하는 메서드
    public void addTransfer(TransferDto transfer) {
        log.info("Adding transfer record: {}", transfer);
        transferMapper.transferInsert(transfer);
        log.info("Added transfer record: {}", transfer);
    }

    // no에 해당하는 게시글을 읽어와 반환하는 메서드
    public TransferDto getTransfer(int no) {
        log.info("Fetching transfer record with no: {}", no);
        TransferDto transfer = transferMapper.transferRead(no);
        log.info("Fetched transfer record: {}", transfer);
        return transfer;
    }

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
