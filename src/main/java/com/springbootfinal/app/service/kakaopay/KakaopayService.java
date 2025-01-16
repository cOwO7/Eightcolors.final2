package com.springbootfinal.app.service.kakaopay;

import com.springbootfinal.app.domain.kakaopay.ApproveRequest;
import com.springbootfinal.app.domain.kakaopay.ReadyRequest;
import com.springbootfinal.app.domain.kakaopay.ReadyResponse;
import com.springbootfinal.app.domain.transfer.TransferDto;
import com.springbootfinal.app.service.transfer.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaopayService {
    @Autowired
    private TransferService transferService;

    @Value("${kakaopay.api.secret.key}")
    private String kakaopaySecretKey;

    @Value("${cid}")
    private String cid;

    @Value("${sample.host}")
    private String sampleHost;

    private String tid;

    public ReadyResponse ready(String agent, String openType, long transferNo) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "DEV_SECRET_KEY " + kakaopaySecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        TransferDto transfer = transferService.getTransfer((int) transferNo);

        ReadyRequest readyRequest = ReadyRequest.builder()
                .cid(cid)
                .partnerOrderId("1")
                .partnerUserId("1")
                .itemName(transfer.getReservationResidName())
                .quantity(1)
                .totalAmount(transfer.getTransferPrice().intValue()) // 수정된 부분
                .taxFreeAmount(0)
                .vatAmount(100)
                .approvalUrl(sampleHost + "/approve/" + agent + "/" + openType)
                .cancelUrl(sampleHost + "/cancel/" + agent + "/" + openType)
                .failUrl(sampleHost + "/fail/" + agent + "/" + openType)
                .build();

        HttpEntity<ReadyRequest> entityMap = new HttpEntity<>(readyRequest, headers);
        ResponseEntity<ReadyResponse> response = new RestTemplate().postForEntity(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
                entityMap,
                ReadyResponse.class
        );
        ReadyResponse readyResponse = response.getBody();

        this.tid = readyResponse.getTid();
        return readyResponse;
    }

    public String approve(String pgToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "SECRET_KEY " + kakaopaySecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ApproveRequest approveRequest = ApproveRequest.builder()
                // ...
                .build();

        HttpEntity<ApproveRequest> entityMap = new HttpEntity<>(approveRequest, headers);
        try {
            ResponseEntity<String> response = new RestTemplate().postForEntity(
                    "https://open-api.kakaopay.com/online/v1/payment/approve",
                    entityMap,
                    String.class
            );
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("API 호출 실패: " + e.getMessage(), e);
        }
    }
}