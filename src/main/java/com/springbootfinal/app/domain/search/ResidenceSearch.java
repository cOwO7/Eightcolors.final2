package com.springbootfinal.app.domain.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResidenceSearch {
	 private Long residNo;
	    private String residName;
	    private String residAddress;
	    private String residType;
	    private int minPrice;  // 기본값을 명확히 하기 위해 int 사용
	    private String thumbnailUrls; // 숙소 대표 이미지 URL 추가

}
