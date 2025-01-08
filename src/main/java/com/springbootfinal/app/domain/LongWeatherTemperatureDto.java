package com.springbootfinal.app.domain;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;

@Data
public class LongWeatherTemperatureDto {
    private Response response;
    private String fcstDate;
    private String fcstTime;

    @Data
    public static class Response {
        private Header header;
        private Body body;
    }

    @Data
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Data
    public static class Body {
        private String dataType;
        private Items items;
        private int numOfRows;
        private int pageNo;
        private int totalCount;
    }

    @Data
    public static class Items {
        private List<Item> item;
    }

    @Data
    public static class Item {
        private String regId;       // 예보구역코드
        private String fcstDate;    // 예보 날짜
        private String fcstTime;    // 예보 시간

        private String taMin4;      // 4일 후 최저기온
        private String taMax4;      // 4일 후 최고기온
        private String taMin5;      // 5일 후 최저기온
        private String taMax5;      // 5일 후 최고기온
        private String taMin6;      // 6일 후 최저기온
        private String taMax6;      // 6일 후 최고기온
        private String taMin7;      // 7일 후 최저기온
        private String taMax7;      // 7일 후 최고기온
        private String taMin8;      // 8일 후 최저기온
        private String taMax8;      // 8일 후 최고기온
        private String taMin9;      // 9일 후 최저기온
        private String taMax9;      // 9일 후 최고기온
        private String taMin10;     // 10일 후 최저기온
        private String taMax10;     // 10일 후 최고기온
        // 배열 형태로 데이터를 가져오는 메서드 추가
        public String getFcstDate(int i) {
            return this.fcstDate;
        }
        public String getFcstTime(int i) {
            return this.fcstTime;
        }

        public String getTaMin(int day) {
            switch (day) {
                case 4: return taMin4;
                case 5: return taMin5;
                case 6: return taMin6;
                case 7: return taMin7;
                case 8: return taMin8;
                case 9: return taMin9;
                case 10: return taMin10;
                default: throw new IllegalArgumentException("Invalid day: " + day);
            }
        }

        public String getTaMax(int day) {
            switch (day) {
                case 4: return taMax4;
                case 5: return taMax5;
                case 6: return taMax6;
                case 7: return taMax7;
                case 8: return taMax8;
                case 9: return taMax9;
                case 10: return taMax10;
                default: throw new IllegalArgumentException("Invalid day: " + day);
            }
        }
    }
}
