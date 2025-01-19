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

    // Constants for pagination
    private static final int PAGE_SIZE = 10;
    private static final int PAGE_GROUP = 10;

    // Constructor for dependency injection
    public TransferService(TransferMapper transferMapper) {
        this.transferMapper = transferMapper;
    }

    // Method to get a paginated and optionally searched list of transfers
    public Map<String, Object> transferList(int pageNum, String search, String searchText) {
    log.info("transferList(int pageNum, String search, String searchText)");
    int currentPage = pageNum;
    int startRow = (currentPage - 1) * PAGE_SIZE;
    log.info("startRow : {}", startRow);
    boolean searchOption = !(search.equals("null") || search.isEmpty());
    int listCount = transferMapper.transferCount(search, searchText);
    List<TransferDto> transferList = transferMapper.transferList(startRow, PAGE_SIZE, search, searchText);
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
    transferMap.put("pageNum", pageNum);
    transferMap.put("search", search);
    transferMap.put("searchText", searchText);
    transferMap.put("searchOption", searchOption);
    transferMap.put("currentPage", currentPage);
    transferMap.put("listCount", listCount);
    transferMap.put("pageGroup", PAGE_GROUP);
    transferMap.put("totalTransferCount", listCount); // Add this line
    log.info("Fetched {} transfer records.", transferList.size());

    return transferMap;
}

    // Method to get a paginated list of transfers without search
/*    public Map<String, Object> transferList(int pageNum) {
        log.info("Fetching transfer list...");
        int startRow = (pageNum - 1) * PAGE_SIZE;
        List<TransferDto> transferList = transferMapper.transferList(startRow, PAGE_SIZE, null, null);
        int totalTransferCount = transferMapper.transferCount(null, null);
        int totalPageCount = (int) Math.ceil((double) totalTransferCount / PAGE_SIZE);
        int startPage = (pageNum - 1) / PAGE_GROUP * PAGE_GROUP + 1;
        int endPage = Math.min(startPage + PAGE_GROUP - 1, totalPageCount);

        Map<String, Object> transferMap = new HashMap<>();
        transferMap.put("transferList", transferList);
        transferMap.put("totalTransferCount", totalTransferCount);
        transferMap.put("totalPageCount", totalPageCount);
        transferMap.put("startPage", startPage);
        transferMap.put("endPage", endPage);
        transferMap.put("pageNum", pageNum);
        log.info("Fetched {} transfer records.", transferList.size());
        return transferMap;
    }*/

    // Method to add a new transfer
    public void addTransfer(TransferDto transfer) {
        log.info("Adding transfer record: {}", transfer);
        transferMapper.transferInsert(transfer);
        log.info("Added transfer record: {}", transfer);
    }

    // Method to get a transfer by its ID
    public TransferDto getTransfer(Long transferNo) {
        log.info("Fetching transfer record with no: {}", transferNo);
        TransferDto transfer = transferMapper.transferRead(transferNo);
        log.info("Fetched transfer record: {}", transfer);
        return transfer;
    }

    // Method to get a list of all transfers
    public List<TransferDto> getTransferList() {
        log.info("Fetching transfer list...");
        List<TransferDto> transferList = transferMapper.transferList();
        log.info("Fetched {} transfer records.", transferList.size());
        return transferList;
    }
}