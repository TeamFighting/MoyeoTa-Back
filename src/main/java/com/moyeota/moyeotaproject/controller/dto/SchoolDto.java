package com.moyeota.moyeotaproject.controller.dto;

import lombok.Builder;
import lombok.Data;

public class SchoolDto {

    @Data
    @Builder
    public static class Request{
        private String key;
        private String email;
        private String univName;
        private boolean univ_check;
        private int code;
    }

    @Data
    @Builder
    public static class RequestForUnivCode {
        private String email;
        private String univName;
    }

    @Data
    @Builder
    public static class RequestForUnivCodeCheck {
        private String email;
        private String univName;
        private int code;
    }

    @Data
    @Builder
    public static class ResponseSuccess {
        private String univName;
        private String certified_email;
        private String certified_date;
    }

}
