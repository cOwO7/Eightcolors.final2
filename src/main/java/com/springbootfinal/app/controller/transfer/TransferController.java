package com.springbootfinal.app.controller.transfer;

import com.springbootfinal.app.domain.transfer.TransferDto;
import com.springbootfinal.app.service.transfer.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TransferController {

    @Autowired
    private TransferService transferService;

    // 게시글 상세보기 요청처리 메서드
    @GetMapping("/transferDetail")
    public String getTransferDetail(Model model, @RequestParam("no") int no) {
        model.addAttribute("transfer", transferService.getTransfer(no));
        return "views/transfer/transferDetail";  // 반환할 뷰 이름 (transferDetail.html)
    }

    @GetMapping("/transfers")
    public String getTransferList(Model model) {
        // transferService에서 transfer 리스트를 조회하고, 모델에 추가
        List<TransferDto> transfers = transferService.getTransferList();  // 메서드 이름을 수정했습니다.
        model.addAttribute("transfers", transfers);
        return "views/transfer/transferList";  // 반환할 뷰 이름 (transferList.html)
    }
}
