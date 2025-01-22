package com.springbootfinal.app.controller.transfer;

import com.springbootfinal.app.domain.transfer.TransferDto;
import com.springbootfinal.app.mapper.ReservationMapper;
import com.springbootfinal.app.service.transfer.TransferService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.PrintWriter;
import java.util.Map;

@Slf4j
@Controller
public class TransferController {

    @Autowired
    private TransferService transferService;
    private static final String TRANSFER_BASE_PATH = "views/transfer/";

    @Autowired
    private ReservationMapper reservationMapper;

    @PostMapping("/delete")
    public String deleteTransfer(RedirectAttributes reAttrs,
            HttpServletResponse response, PrintWriter out,
            @RequestParam("transferNo") long transferNo,
                                 @RequestParam(value = "pageCount",
                                         defaultValue = "1") int pageCount) {

        transferService.deleteTransfer(transferNo);
        reAttrs.addAttribute("pageCount", pageCount);
        return "redirect:/transfers";

        }

    // 양도 수정 폼 요청 처리 메서드
    @PostMapping("/updateForm")
    public String updateTransferForm(Model model,
                                     HttpServletResponse response, PrintWriter out,
                                     @RequestParam("transferNo") long transferNo,
                                     @RequestParam(value = "pageCount", defaultValue = "1") int pageCount) {

        TransferDto transfer = transferService.getTransfer(transferNo, false);
        model.addAttribute("transfer", transfer);
        model.addAttribute("pageCount", pageCount);

        return TRANSFER_BASE_PATH + "transferUpdate";

  /*      boolean searchOption = !(search.equals("null") || search.isEmpty());

        model.addAttribute("transfer", transfer); // transfer 객체를 모델에 추가
        model.addAttribute("transferNo", transferNo);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("searchOption", searchOption);

        if (searchOption) {
            model.addAttribute("search", search);
            model.addAttribute("keyword", keyword);
        }
        return TRANSFER_BASE_PATH + "transferUpdate";*/
    }

@PostMapping("/update")
public String updateBoard(TransferDto transferDto, RedirectAttributes reAttrs,
                          @RequestParam(value = "pageCount", defaultValue = "1") int pageCount,
                          HttpServletResponse response, PrintWriter out) {
    transferService.updateBoard(transferDto);
    reAttrs.addAttribute("pageCount", pageCount);
    reAttrs.addAttribute("test1", "1회성 파라미터");
    return "redirect:/transfers";
}

    // 양도 생성 폼 요청 처리 메서드

@GetMapping("/transferWrite")
public String createTransferForm(Model model, HttpServletRequest request) {
    Logger logger = LoggerFactory.getLogger(TransferController.class);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // 세션에서 userNo 가져오기
    Object userNoObj = request.getSession().getAttribute("userNo");
    String userNo = userNoObj != null ? userNoObj.toString() : null;

    log.info("User logged in: userNo={}, role={}", userNo, authentication.getAuthorities());
    log.info("Authenticated userNo: {}", userNo);

    int reservationCount = reservationMapper.countReservationsByUserNo(userNo);
    log.info("Reservation count for userNo {}: {}", userNo, reservationCount);

    if (reservationCount > 0) {
        return TRANSFER_BASE_PATH + "transferWrite";
    } else {
        model.addAttribute("errorMessage", "예약 내역이 있는 회원만 글쓰기가 가능합니다.");
        return TRANSFER_BASE_PATH + "errorPage";
    }
}

    // 게시글 쓰기 요청 처리 메서드
    @PostMapping("/transferAdd")
    public String addTransfer(TransferDto transfer) {
        transferService.addTransfer(transfer);
        return "redirect:/transfers";
    }

 /*   // 게시글 상세보기 요청 처리 메서드
    @GetMapping("/transferDetail")
    public String getTransferDetail(Model model, @RequestParam("transferNo") long transferNo) {
        model.addAttribute("transfer", transferService.getTransfer(transferNo, false));
        return TRANSFER_BASE_PATH + "transferDetail";
    }  */
    // 게시글 상세보기 요청 처리 메서드
    @GetMapping("/transferDetail")
    public String getTransferDetail(Model model, @RequestParam("transferNo") long transferNo,
                                    @RequestParam(value = "pageCount", defaultValue = "1") int pageCount,
                                    @RequestParam(value = "type", defaultValue = "") String type,
                                    @RequestParam(value = "keyword", defaultValue = "") String keyword) {

        boolean searchOption = !(type.isEmpty() || keyword.isEmpty());

        TransferDto transfer = transferService.getTransfer(transferNo, true);
        model.addAttribute("transfer", transfer);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("searchOption", searchOption);

        if (searchOption) {
            model.addAttribute("type", type);
            model.addAttribute("keyword", keyword);
        }

        return TRANSFER_BASE_PATH + "transferDetail";
    }

    // 게시글 목록 요청 처리 메서드
    @GetMapping("/transfers")
    public String getTransferList(Model model,
                                  @RequestParam(value = "pageCount", required = false, defaultValue = "1") int pageCount,
                                  @RequestParam(value = "type", required = false, defaultValue = "null") String type,
                                  @RequestParam(value = "keyword", required = false, defaultValue = "null") String keyword) {
        Map<String, Object> modelMap = transferService.transferList(pageCount, type, keyword);
        model.addAllAttributes(modelMap);
        return TRANSFER_BASE_PATH + "transferList";
    }
}