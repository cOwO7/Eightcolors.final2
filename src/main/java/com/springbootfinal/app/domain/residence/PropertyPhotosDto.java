package com.springbootfinal.app.domain.residence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    public List<String> getAllPhotoUrls() {
        List<String> urls = new ArrayList<>();
        if (photoUrl01 != null) urls.add(photoUrl01);
        if (photoUrl02 != null) urls.add(photoUrl02);
        if (photoUrl03 != null) urls.add(photoUrl03);
        if (photoUrl04 != null) urls.add(photoUrl04);
        if (photoUrl05 != null) urls.add(photoUrl05);
        if (photoUrl06 != null) urls.add(photoUrl06);
        if (photoUrl07 != null) urls.add(photoUrl07);
        if (photoUrl08 != null) urls.add(photoUrl08);
        if (photoUrl09 != null) urls.add(photoUrl09);
        if (photoUrl10 != null) urls.add(photoUrl10);
        return urls;
    }
}
