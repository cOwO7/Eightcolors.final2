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

    //게시글 쓰기 폼에서 들어오는 게시글 쓰기 요청을 처리하는 메서드
    @PostMapping("/addTransfer")
    public String addTransfer(TransferDto transfer) {
        transferService.addTransfer(transfer);
        return "redirect:/transfers";  // 반환할 뷰 이름 (transferList.html)
    }

    // 게시글 상세보기 요청처리 메서드
    @GetMapping("/transferDetail")
    public String getTransferDetail(Model model, @RequestParam("no") int no) {
        model.addAttribute("transfer", transferService.getTransfer(no));
        return "views/transfer/transferDetail";  // 반환할 뷰 이름 (transferDetail.html)
    }

   @GetMapping("/transfers")
    public String getTransferList(Model model,
                              @RequestParam(value = "pageNum", required = false,
                                      defaultValue = "1") int pageNum) {
    Map<String, Object> modelMap = transferService.transferList(pageNum);
    model.addAllAttributes(modelMap);
    model.addAttribute("transfers", modelMap.get("transferList")); // transfers 변수 추가
    return "views/transfer/transferList";  // 반환할 뷰 이름 (transferList.html)
    }

    // 양도 생성 폼 요청 처리 메서드
    @GetMapping("/transferWrite")
    public String createTransferForm() {
        return "views/transfer/transferWrite";  // 반환할 뷰 이름 (transferWrite.html)
    }

    // 게시글 쓰기 요청 처리 메서드
    @PostMapping("/transferAdd")
    public String addBoard(TransferDto transfer) {
        transferService.addTransfer(transfer);
        return "redirect:/transfers";  // 반환할 뷰 이름 (transferList.html)
    }
}