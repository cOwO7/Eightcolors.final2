package com.springbootfinal.app.service.kakaopay;

import com.springbootfinal.app.domain.kakaopay.ApproveRequest;
import com.springbootfinal.app.domain.kakaopay.ReadyRequest;
import com.springbootfinal.app.domain.kakaopay.ReadyResponse;
import com.springbootfinal.app.domain.reservations.Reservations;
import com.springbootfinal.app.domain.transfer.TransferDto;
import com.springbootfinal.app.mapper.ReservationMapper;
import com.springbootfinal.app.mapper.transfer.TransferMapper;
import com.springbootfinal.app.service.transfer.TransferService;
import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class KakaopayService {
    private static final Logger log = LoggerFactory.getLogger(KakaopayService.class);

    @Autowired
    private TransferService transferService;

    @Autowired
    private TransferMapper transferMapper;

    @Autowired
    private ReservationMapper reservationsMapper;

    @Value("${kakaopay.api.secret.key}")
    private String kakaopaySecretKey;

    @Value("${cid}")
    private String cid;

    @Value("${sample.host}")
    private String sampleHost;
    private Long transferNo;
    private String tid;
    private String partnerUserId;
    private String partnerOrderId;

    public ReadyResponse ready(String agent, String openType, long transferNo, String userNo) {
        this.transferNo = transferNo;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "DEV_SECRET_KEY " + kakaopaySecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        TransferDto transfer = transferService.getTransfer(transferNo, false);

        ReadyRequest readyRequest = ReadyRequest.builder()
                .cid(cid)
                .partnerOrderId("1")
                .partnerUserId(userNo)
                .itemName(transfer.getReservationResidName())
                .quantity(1)
                .totalAmount(transfer.getTransferPrice().intValue())
                .taxFreeAmount(0)
                .vatAmount(100)
                .approvalUrl(sampleHost + "/approve/" + agent + "/" + openType)
                .cancelUrl(sampleHost + "/cancel/" + agent + "/" + openType)
                .failUrl(sampleHost + "/fail/" + agent + "/" + openType)
                .build();

        log.info("KakaoPay ReadyRequest: {}", readyRequest);

        HttpEntity<ReadyRequest> entityMap = new HttpEntity<>(readyRequest, headers);
        ResponseEntity<ReadyResponse> response = new RestTemplate().postForEntity(
                "https://open-api.kakaopay.com/online/v1/payment/ready", entityMap,
                ReadyResponse.class);
        ReadyResponse readyResponse = response.getBody();

        this.tid = readyResponse.getTid();
        this.partnerOrderId = readyRequest.getPartnerOrderId();
        this.partnerUserId = readyRequest.getPartnerUserId();
        return readyResponse;
    }

    public String approve(String pgToken, Long userNo) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "SECRET_KEY " + kakaopaySecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ApproveRequest approveRequest = ApproveRequest.builder()
                .tid(tid)
                .cid(cid)
                .partnerOrderId(partnerOrderId)
                .partnerUserId(partnerUserId)
                .pgToken(pgToken)
                .build();
        HttpEntity<ApproveRequest> entityMap = new HttpEntity<>(approveRequest, headers);
        try {
            ResponseEntity<String> response = new RestTemplate().postForEntity(
                    "https://open-api.kakaopay.com/online/v1/payment/approve",
                    entityMap,
                    String.class);
//            // 결제 승인 후 양도 상태를 "양도 완료"로 업데이트
//            updateTransferStatus(approveRequest.getPartnerOrderId(), "양도 완료");
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("API 호출 실패: " + e.getMessage(), e);
        } finally {
            TransferDto transfer = transferMapper.transferRead(this.transferNo, false);
            Reservations param = new Reservations();
            param.setReservationNo(transfer.getReservationNo());
            param.setUserNo(userNo);
            reservationsMapper.putReservations(param);

            TransferDto transParam = new TransferDto();
            transParam.setTransferNo(transfer.getTransferNo());
            transParam.setBuyerUserNo(userNo);
            transferMapper.putTransfer(transParam);
        }
    }

    public void updateTransferStatus(String partnerOrderId, String status) {
        TransferDto transfer = transferMapper.findByPartnerOrderId(partnerOrderId);
        if (transfer != null) {
            log.info("양도게시판 상태 변경 for order ID {}: {}", partnerOrderId, status);
            transferMapper.updateTransferStatus(partnerOrderId, status);
        } else {
            log.warn("Transfer not found for order ID {}", partnerOrderId);
        }
    }
}