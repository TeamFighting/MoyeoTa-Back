package com.moyeota.moyeotaproject.controller.dto;

import lombok.Builder;
import lombok.Data;

public class SignUpDto {

    @Data
    @Builder
    public static class Request {
        private String loginId;
        private String password;
        private String name;
        private String profileImage;
        private String phoneNumber;
        private String email;
        private String status;
        private Float averageStarRate;
        private String school;
        private Boolean gender;
    }


}
