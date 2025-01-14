package com.springbootfinal.app.domain.residence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyPhotosDto {

    private Long photoNo;
    private Long residNo;  // 외래 키
    private String thumbnailUrls;
    private String photoUrl01;
    private String photoUrl02;
    private String photoUrl03;
    private String photoUrl04;
    private String photoUrl05;
    private String photoUrl06;
    private String photoUrl07;
    private String photoUrl08;
    private String photoUrl09;
    private String photoUrl10;
}
