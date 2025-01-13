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

    private Long photoNo;        // 사진 ID (PK)
    private Long residNo;        // 숙소 번호 (FK)
    private String thumbnailUrls;  // 썸네일 URL
    private String photoUrl01;   // 사진 URL 1
    private String photoUrl02;   // 사진 URL 2
    private String photoUrl03;   // 사진 URL 3
    private String photoUrl04;   // 사진 URL 4
    private String photoUrl05;   // 사진 URL 5
    private String photoUrl06;   // 사진 URL 6
    private String photoUrl07;   // 사진 URL 7
    private String photoUrl08;   // 사진 URL 8
    private String photoUrl09;   // 사진 URL 9
    private String photoUrl10;   // 사진 URL 10
}
