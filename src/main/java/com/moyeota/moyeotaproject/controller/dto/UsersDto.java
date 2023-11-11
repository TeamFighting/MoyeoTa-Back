package com.moyeota.moyeotaproject.controller.dto;

import lombok.Builder;
import lombok.Data;

public class UsersDto {

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

    @Data
    @Builder
    public static class Response {
        private String loginId;
        private String name;
        private String profileImage;
        private String phoneNumber;
        private String email;
        private String status;
        private Float averageStarRate;
        private String school;
        private Boolean gender;
    }

    @Data
    @Builder
    public static class updateDto {
        private String name;
        private String profileImage;
        private String phoneNumber;
        private String email;
        private Boolean gender;
    }

    @Data
    @Builder
    public static class deleteDto {
        private String name;
        private String email;
    }


}
