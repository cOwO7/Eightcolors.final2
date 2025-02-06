package com.springbootfinal.app.domain.mainpage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainPage {
    private Long id;
    private String name;
    private String photoUrl01;

    // Method to get full image path
    public String getFullPhotoUrl01() {
        if (this.photoUrl01 == null || this.photoUrl01.isEmpty()) {
            return "/image/sample/sample1.jpg";
        }
        return "/images/files/" + this.photoUrl01;
    }
}