package com.springbootfinal.app.controller.transfer;

import com.springbootfinal.app.domain.transfer.TransferDto;
import com.springbootfinal.app.service.transfer.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class TransferController {

    @Autowired
    private TransferService transferService;
    private static final String TRANSFER_BASE_PATH = "views/transfer/";

    // 양도 생성 폼 요청 처리 메서드
   @GetMapping("/transferWrite")
    public String createTransferForm() {
    return TRANSFER_BASE_PATH + "transferWrite";  // transferWrite.html
}

    // 게시글 쓰기 요청 처리 메서드
    @PostMapping("/transferAdd")
    public String addTransfer(TransferDto transfer) {
        transferService.addTransfer(transfer);
        return "redirect:/transfers";  // Redirect to /transfers
    }

    // 게시글 상세보기 요청 처리 메서드
    @GetMapping("/transferDetail")

    public String getTransferDetail(Model model, @RequestParam("residNo") long residNo) {
        model.addAttribute("transfer", transferService.getTransfer(residNo));
        return TRANSFER_BASE_PATH + "transferDetail";  // Return view name (transferDetail.html)
    }
    // 게시글 목록 요청 처리 메서드
    @GetMapping("/transfers")
    public String getTransferList(Model model,
                                  @RequestParam(value = "pageCount", required = false, defaultValue = "1") int pageCount,
                                  @RequestParam(value = "search", required = false, defaultValue = "") String search,
                                  @RequestParam(value = "searchText", required = false, defaultValue = "") String searchText) {
        Map<String, Object> modelMap = transferService.transferList(pageCount, search, searchText);
        model.addAllAttributes(modelMap);
        return TRANSFER_BASE_PATH + "transferList";  // transferList.html
    }
}
