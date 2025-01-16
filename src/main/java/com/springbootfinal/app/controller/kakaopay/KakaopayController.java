package com.springbootfinal.app.controller.kakaopay;

import com.springbootfinal.app.domain.kakaopay.ReadyResponse;
import com.springbootfinal.app.service.kakaopay.KakaopayService;
import com.springbootfinal.app.service.transfer.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KakaopayController {
    @Autowired
    private KakaopayService kakaoPayService;
    private TransferService transferService;

    @GetMapping("/Kakaopay")
    public String index() {
        return "pc/index";
    }

    // 결제 준비 요청 transferNo를 받아서 결제 준비 요청을 한다.
    // Request payment preparation with transferNo

    @GetMapping("/ready/{agent}/{openType}")
    public String ready(@PathVariable("agent") String agent, @PathVariable("openType") String openType,
                        @RequestParam("transferNo") long transferNo, Model model) {
        ReadyResponse readyResponse = kakaoPayService.ready(agent, openType, transferNo);

        if (agent.equals("mobile")) {
            return "redirect:" + readyResponse.getNext_redirect_mobile_url();
        }

        if (agent.equals("app")) {
            model.addAttribute("webviewUrl", "app://webview?url=" + readyResponse.getNext_redirect_app_url());
            return "app/webview/ready";
        }

        model.addAttribute("response", readyResponse);
        return agent + "/" + openType + "/ready";
    }

    @GetMapping("/approve/{agent}/{openType}")
    public String approve(@PathVariable("agent") String agent, @PathVariable("openType") String openType, @RequestParam("pg_token") String pgToken, Model model) {
        String approveResponse = kakaoPayService.approve(pgToken);
        model.addAttribute("response", approveResponse);
        return agent + "/" + openType + "/approve";
    }

    @GetMapping("/cancel/{agent}/{openType}")
    public String cancel(@PathVariable("agent") String agent, @PathVariable("openType") String openType) {
        // 주문건이 진짜 취소되었는지 확인 후 취소 처리
        // 결제내역조회(/v1/payment/status) api에서 status를 확인한다.
        // To prevent the unwanted request cancellation caused by attack,
        // the “show payment status” API is called and then check if the status is QUIT_PAYMENT before suspending the payment
        return agent + "/" + openType + "/cancel";
    }

    @GetMapping("/fail/{agent}/{openType}")
    public String fail(@PathVariable("agent") String agent, @PathVariable("openType") String openType) {
        // 주문건이 진짜 실패되었는지 확인 후 실패 처리
        // 결제내역조회(/v1/payment/status) api에서 status를 확인한다.
        // To prevent the unwanted request cancellation caused by attack,
        // the “show payment status” API is called and then check if the status is FAIL_PAYMENT before suspending the payment
        return agent + "/" + openType + "/fail";
    }
}