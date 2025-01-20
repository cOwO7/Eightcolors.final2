package com.springbootfinal.app.controller.transfer;

import com.springbootfinal.app.domain.transfer.TransferDto;
import com.springbootfinal.app.service.transfer.TransferService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.PrintWriter;
import java.util.Map;

@Controller
public class TransferController {

    @Autowired
    private TransferService transferService;
    private static final String TRANSFER_BASE_PATH = "views/transfer/";

    // 양도 수정 폼 요청 처리 메서드
    @PostMapping("updateForm")
    public String updateTransferForm(Model model,
                                     HttpServletResponse response, PrintWriter out,
                                     @RequestParam("transferNo") long transferNo,
                                     @RequestParam(value="pageCount", required = false, defaultValue = "1") int pageCount,
                                     @RequestParam(value="search", required = false, defaultValue = "") String search,
                                     @RequestParam(value="keyword", required = false, defaultValue = "") String keyword) {

        TransferDto transfer = transferService.getTransfer(transferNo);
        boolean searchOption = !(search.equals("null") || search.isEmpty());

        model.addAttribute("transfer", transfer); // transfer 객체를 모델에 추가
        model.addAttribute("transferNo", transferNo);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("searchOption", searchOption);

        if (searchOption) {
            model.addAttribute("search", search);
            model.addAttribute("keyword", keyword);
        }
        return TRANSFER_BASE_PATH + "transferUpdate";
    }

    @PostMapping("transferUpdate")
    public String transferUpdate(TransferDto transferDto, RedirectAttributes reAttrs,
                                 HttpServletResponse response, PrintWriter out,
                                 @RequestParam("transferNo") long transferNo,
                                 @RequestParam("pageCount") int pageCount,
                                 @RequestParam("search") String search,
                                 @RequestParam("keyword") String keyword,
                                 @RequestParam("transferPrice") int transferPrice,
                                 @RequestParam("transferTitle") String transferTitle,
                                 @RequestParam("content") String content) {

        boolean searchOption = (search.equals("null") || keyword.equals("null")) ? false : true;

        reAttrs.addAttribute("searchOption", searchOption);
        reAttrs.addAttribute("pageCount", pageCount);

        if (searchOption) {
            reAttrs.addAttribute("search", search);
            reAttrs.addAttribute("keyword", keyword);
        }
        return "redirect:/transfers";
    }

    // 양도 생성 폼 요청 처리 메서드
    @GetMapping("/transferWrite")
    public String createTransferForm() {
        return TRANSFER_BASE_PATH + "transferWrite";
    }

    // 게시글 쓰기 요청 처리 메서드
    @PostMapping("/transferAdd")
    public String addTransfer(TransferDto transfer) {
        transferService.addTransfer(transfer);
        return "redirect:/transfers";
    }

    // 게시글 상세보기 요청 처리 메서드
    @GetMapping("/transferDetail")
    public String getTransferDetail(Model model, @RequestParam("transferNo") long transferNo) {
        model.addAttribute("transfer", transferService.getTransfer(transferNo));
        return TRANSFER_BASE_PATH + "transferDetail";
    }

    // 게시글 목록 요청 처리 메서드
    @GetMapping("/transfers")
    public String getTransferList(Model model,
                                  @RequestParam(value = "pageCount", required = false, defaultValue = "1") int pageCount,
                                  @RequestParam(value = "search", required = false, defaultValue = "") String search,
                                  @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) {
        Map<String, Object> modelMap = transferService.transferList(pageCount, search, keyword);
        model.addAllAttributes(modelMap);
        return TRANSFER_BASE_PATH + "transferList";
    }
}