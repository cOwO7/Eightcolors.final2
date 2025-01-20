package com.springbootfinal.app.service.transfer;

import com.springbootfinal.app.domain.transfer.TransferDto;
import com.springbootfinal.app.mapper.transfer.TransferMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TransferService {

    private final TransferMapper transferMapper;

    public void deleteTransfer(Long transferNo) {
        log.info("TransferService: 삭제하기{}", transferNo);
        transferMapper.deleteTransfer(transferNo);
    }

    public void updateBoard(TransferDto transferDto) {
        log.info("TransferService: 수정하기{}", transferDto);
        transferMapper.updateBoard(transferDto);
    }


    private static final int PAGE_SIZE = 10;
    private static final int PAGE_GROUP = 10;


    public TransferService(TransferMapper transferMapper) {
        this.transferMapper = transferMapper;
    }


    public Map<String, Object> transferList(int pageNum, String search, String keyword) {
        log.info("transferList(int pageCount, 문자열 검색, 문자열 키워드");
        int currentPage = pageNum;
        int startRow = (currentPage - 1) * PAGE_SIZE;
        log.info("startRow : {}", startRow);
        boolean searchOption = !(search.equals("null") || search.isEmpty());
        int listCount = transferMapper.transferCount(search, keyword);
        List<TransferDto> transferList = transferMapper.transferList(startRow, PAGE_SIZE, search, keyword);
        int pageCount = listCount / PAGE_SIZE + (listCount % PAGE_SIZE == 0 ? 0 : 1);
        int startPage = (currentPage / PAGE_GROUP) * PAGE_GROUP + 1 - (currentPage % PAGE_GROUP == 0 ? PAGE_GROUP : 0);
        int endPage = startPage + PAGE_GROUP - 1;

        if (endPage > pageCount) {
            endPage = pageCount;
        }

        Map<String, Object> transferMap = new HashMap<>();

        transferMap.put("transferList", transferList);
        transferMap.put("startPage", startPage);
        transferMap.put("endPage", endPage);
        transferMap.put("pageCount", pageCount);
        transferMap.put("search", search);
        transferMap.put("keyword", keyword);
        transferMap.put("searchOption", searchOption);
        transferMap.put("currentPage", currentPage);
        transferMap.put("listCount", listCount);
        transferMap.put("pageGroup", PAGE_GROUP);
        transferMap.put("totalTransferCount", listCount);
        log.info("Fetched {} transfer records.", transferList.size());

        if (searchOption) {
            transferMap.put("search", search);
            transferMap.put("keyword", keyword);
        }

        return transferMap;
    }


    public void addTransfer(TransferDto transfer) {
        log.info("Adding transfer record: {}", transfer);
        transferMapper.transferInsert(transfer);
        log.info("Added transfer record: {}", transfer);
    }


    public TransferDto getTransfer(Long transferNo) {
        log.info("양도게시글 번호{}", transferNo);
        TransferDto transfer = transferMapper.transferRead(transferNo);
        log.info("Fetched transfer record: {}", transfer);
        return transfer;

    }

    public List<TransferDto> getTransferList() {
        log.info("양도게시글 리스트 받아오기 완료");
        List<TransferDto> transferList = transferMapper.transferList();
        log.info("게시글 사이즈", transferList.size());
        return transferList;
    }
}