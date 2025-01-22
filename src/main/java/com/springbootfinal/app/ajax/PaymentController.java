package com.springbootfinal.app.ajax;

import java.io.IOException;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import jakarta.servlet.http.HttpSession;

@Controller
@Slf4j
public class PaymentController {
	
	 private final IamportClient iamportClient;

	    public PaymentController() {
	        this.iamportClient = new IamportClient("1171411166060454",
	                "hba2DdlMh2TmgvGUPYOP3BpZpnJoje3vw9tRAzMmpIKnFrK2uD2icK8KAFBXLOzapuOky14DD1hVH9P4");
	    }

	    @ResponseBody
	    @RequestMapping("/verify/{imp_uid}")
	    public IamportResponse<Payment> paymentByImpUid(@PathVariable("imp_uid") String imp_uid)
	            throws IamportResponseException, IOException {
	        return iamportClient.paymentByImpUid(imp_uid);
	    }
	    
	    @GetMapping("/test")
	    public String test() {
	    	return "test2";
	    }

	    
	    // 결제 성공 데이터 처리 및 성공 페이지로 리다이렉트
	    @PostMapping("/payment/success")
	    @ResponseBody
	    public String handlePaymentSuccess(@RequestBody Map<String, Object> paymentData, HttpSession session) {
	        /*session.setAttribute("impUid", paymentData.get("imp_uid"));
	        session.setAttribute("merchantUid", paymentData.get("merchant_uid"));
	        session.setAttribute("paidAmount", paymentData.get("paid_amount"));
	        session.setAttribute("buyerName", paymentData.get("buyer_name"));
	        session.setAttribute("buyerEmail", paymentData.get("buyer_email"));*/

			session.setAttribute("roomNo",paymentData.get("roomNo"));
			session.setAttribute("checkinDate",paymentData.get("checkinDate"));
			session.setAttribute("checkoutDate",paymentData.get("checkoutDate"));
			session.setAttribute("residNo",paymentData.get("residNo"));

			log.info("체크인날짜 : "+(String) paymentData.get("checkinDate"));

	        return "/reserve";
	    }

	/*    @GetMapping("/payment/success-page")
	    public String renderSuccessPage(Model model, HttpSession session) {
	        model.addAttribute("impUid", session.getAttribute("impUid"));
	        model.addAttribute("merchantUid", session.getAttribute("merchantUid"));
	        model.addAttribute("paidAmount", session.getAttribute("paidAmount"));
	        model.addAttribute("buyerName", session.getAttribute("buyerName"));
	        model.addAttribute("buyerEmail", session.getAttribute("buyerEmail"));

	        return "paymentSuccess"; // Thymeleaf 뷰 이름
	    }*/


}
