package com.springbootfinal.app.controller.transfer;

import com.springbootfinal.app.domain.reservations.Reservations;
import com.springbootfinal.app.domain.transfer.TransferDto;
import com.springbootfinal.app.mapper.ReservationMapper;
import com.springbootfinal.app.mapper.residence.ResidenceRoomMapper;
import com.springbootfinal.app.service.transfer.TransferService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class TransferController {

    @Autowired
    private TransferService transferService;
    private static final String TRANSFER_BASE_PATH = "views/transfer/";

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private ResidenceRoomMapper roomMapper;

    @Autowired
    private ReservationMapper resvMapper;

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

        logger.info("사용자 로그인 : 회원번호={}, role={}", userNo, authentication.getAuthorities());
        logger.info("검증된 회원번호: {}", userNo);

        int reservationCount = reservationMapper.countReservationsByUserNo(userNo);
        logger.info("회원번호의 예약 수 {}: {}", userNo, reservationCount);

        if (reservationCount > 0) {
            // 예약 내역 가져오기
            Long userNoLong = Long.parseLong(userNo);
            Reservations reservation = transferService.getReservationByUserNo(userNoLong);
            logger.info("예약 상태: {}", reservation); // 예약 정보 로그 추가

            // 예약 번호로 이미 작성된 양도 게시글이 있는지 확인
            if (transferService.isTransferExistsByReservationNo(reservation.getReservationNo())) {
                String errorMessage = "해당 예약으로 이미 양도 게시글이 작성되었습니다.";
                model.addAttribute("script", "<script>alert('" + errorMessage + "'); history.back();</script>");
                return TRANSFER_BASE_PATH + "errorPage"; // errorPage.jsp 또는 템플릿 파일로 연결
            }

            model.addAttribute("reservation", reservation);
            return TRANSFER_BASE_PATH + "transferWrite";
        } else {
            String errorMessage = "예약 내역이 있는 회원만 글쓰기가 가능합니다.";
            model.addAttribute("script", "<script>alert('" + errorMessage + "'); history.back();</script>");
            return TRANSFER_BASE_PATH + "errorPage"; // errorPage.jsp 또는 템플릿 파일로 연결
        }
    }

    // 게시글 쓰기 요청 처리 메서드
    @PostMapping("/transferAdd")
    public String addTransfer(TransferDto transfer, HttpSession httpSession) {
        Long userNo = (Long) httpSession.getAttribute("userNo");
        Reservations reservation = transferService.getReservationByUserNo(userNo);
        log.info("예약 상태: {}", reservation);

        Long reservationNo = reservation.getReservationNo();
        log.info("예약번호: {}", reservationNo);

        // Delete existing transfers with the same reservation_no
        transferService.deleteTransferByReservationNo(reservationNo);

        // Set the necessary fields and add the new transfer
        transfer.setSellerUserNo(userNo);
        transfer.setReservationNo(reservationNo);
        transferService.addTransfer(transfer);

        return "redirect:/transfers";
    }
    // 게시글 상세보기 요청 처리 메서드
    @GetMapping("/transferDetail")
    public String getTransferDetail(Model model, @RequestParam("transferNo") long transferNo,
                                    @RequestParam(value = "pageCount", defaultValue = "1") int pageCount,
                                    @RequestParam(value = "type", defaultValue = "") String type,
                                    @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                    HttpSession session) {
        boolean searchOption = !(type.isEmpty() || keyword.isEmpty());
        TransferDto transfer = transferService.getTransfer(transferNo, true);

        // userNo와 hostUserNo 로그 추가 (디버깅 용도)
        log.warn("userNo: " + session.getAttribute("userNo"));
        log.warn("hostUserNo: " + session.getAttribute("hostUserNo"));
        log.warn("adminUserNo: " + session.getAttribute("adminUserNo"));

        Reservations resvParam = new Reservations();
        resvParam.setReservationNo(transfer.getReservationNo());
        List<Reservations> resvRs = resvMapper.getReservations(resvParam);

        log.warn("resvNo : " + transfer.getReservationNo());
        log.warn("reservations : " + resvRs.get(0).toString());

        transfer.setRoomNo(resvRs.get(0).getRoomNo().toString());

        // 모델에 userNo와 hostUserNo 추가
        model.addAttribute("userNo", session.getAttribute("userNo"));
        model.addAttribute("hostUserNo", session.getAttribute("hostUserNo"));
        model.addAttribute("adminUserNo", session.getAttribute("adminUserNo"));
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