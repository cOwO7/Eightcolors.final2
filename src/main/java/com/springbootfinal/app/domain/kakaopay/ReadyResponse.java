package com.springbootfinal.app.domain.kakaopay; // 패키지 선언

import lombok.Data;

@Data
public class ReadyResponse { // ReadyResponse 클래스 선언
    private String tid; // 거래 ID
    private Boolean tms_result; // TMS 결과
    private String created_at; // 생성 시간
    private String next_redirect_pc_url; // PC 리다이렉트 URL
    private String next_redirect_mobile_url; // 모바일 리다이렉트 URL
    private String next_redirect_app_url; // 앱 리다이렉트 URL
    private String android_app_scheme; // 안드로이드 앱 스킴
    private String ios_app_scheme; // iOS 앱 스킴

}