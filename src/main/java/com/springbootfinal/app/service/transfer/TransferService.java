package com.springbootfinal.app.service.transfer;

import com.springbootfinal.app.domain.reservations.Reservations;
import com.springbootfinal.app.domain.transfer.TransferDto;
import com.springbootfinal.app.mapper.ReservationMapper;
import com.springbootfinal.app.mapper.transfer.TransferMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TransferService {

    private final TransferMapper transferMapper;

    @Autowired
    private ReservationMapper reservationMapper;



/*    public TransferDto getTransferDto(Long transferNo, boolean isCount) {
        log.info("getTransferDto: {}, Long transferNo, boolean isCount", transferNo, isCount);
        TransferDto transfer = transferMapper.transferRead(transferNo);
        log.info("transferRead: {}", transferNo);

        if (isCount) {
            transferMapper.incrementReadCount(transferNo);
        }

        return transferMapper.transferRead(transferNo);

    }*/


    public void updateTransferStatus(String partnerOrderId, String status) {
        TransferDto transfer = transferMapper.findByPartnerOrderId(partnerOrderId);
        if (transfer != null) {
            transferMapper.updateTransferStatus(partnerOrderId, status);
        }
    }


    public void deleteTransfer(Long transferNo) {
        log.info("TransferService: 삭제하기{}", transferNo);
        transferMapper.deleteTransfer(transferNo);
    }

    public void updateBoard(TransferDto transferDto) {
        log.info("TransferService: 수정하기{}", transferDto);
        transferMapper.updateBoard(transferDto);
    }


    private static final int PAGE_SIZE = 20;
    private static final int PAGE_GROUP = 20;


    public TransferService(TransferMapper transferMapper) {
        this.transferMapper = transferMapper;
    }


    public Map<String, Object> transferList(int pageCount, String type, String keyword) {
        log.info("transferList(int pageCount, String type, String keyword");
        int currentPage = pageCount;
        int startRow = (currentPage - 1) * PAGE_SIZE;
        log.info("startRow : {}", startRow);
        boolean searchOption = (type.equals("null") || keyword.equals("null")) ? false : true;
        int listCount = transferMapper.getTransferCount(type, keyword);
        List<TransferDto> transferList = transferMapper.transferList(startRow, PAGE_SIZE, type, keyword);
        int totalPageCount = listCount / PAGE_SIZE + (listCount % PAGE_SIZE == 0 ? 0 : 1);
        int startPage = (currentPage / PAGE_GROUP) * PAGE_GROUP + 1 - (currentPage % PAGE_GROUP == 0 ? PAGE_GROUP : 0);
        int endPage = startPage + PAGE_GROUP - 1;

        if (endPage > totalPageCount) {
            endPage = totalPageCount;
        }

        Map<String, Object> modelMap = new HashMap<String, Object>();

        modelMap.put("transferList", transferList);
        modelMap.put("totalPageCount", totalPageCount);
        modelMap.put("startPage", startPage);
        modelMap.put("endPage", endPage);
        modelMap.put("currentPage", currentPage);
        modelMap.put("listCount", listCount);
        modelMap.put("pageGroup", PAGE_GROUP);
        modelMap.put("searchOption", searchOption);
        log.info("transferList", transferList.size());

        if (searchOption) {
            modelMap.put("type", type);
            modelMap.put("keyword", keyword);
        }

        return modelMap;
    }


    public void addTransfer(TransferDto transfer) {
        log.info("게시글 추가", transfer);
        transferMapper.transferInsert(transfer);
        log.info("게시글 transfers DB 저장완료", transfer);
    }

    public Reservations getReservationByUserNo(Long userNo) {
        return reservationMapper.getReservationByUserNo(userNo);
    }


    /* public TransferDto getTransfer(Long transferNo, boolean isCount) {
         log.info("getTransfer: {}", transferNo);
         TransferDto transfer = transferMapper.transferRead(transferNo , isCount);
         log.info("getTransfer: {}", transfer);
         return transfer;

     }*/
    public TransferDto getTransfer(Long transferNo, boolean isCount) {
        log.info("getTransfer: {}", transferNo);
        if(isCount) {
            transferMapper.incrementReadCount(transferNo);
        }
        return transferMapper.transferRead(transferNo, false);
    }

    public List<TransferDto> getTransferList() {
        log.info("양도게시글 리스트 받아오기 완료");
        List<TransferDto> transferList = transferMapper.transferList();
        log.info("게시글 사이즈", transferList.size());
        return transferList;
    }
}