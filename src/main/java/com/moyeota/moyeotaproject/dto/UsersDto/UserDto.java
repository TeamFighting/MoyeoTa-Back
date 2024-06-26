package com.moyeota.moyeotaproject.dto.UsersDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class UserDto {

    @Data
    @Builder
    public static class Request {
        private String loginId;
        private String password;
        private String name;
        private String nickName;
        private String age;
        private String profileImage;
        private String phoneNumber;
        private String email;
        private String status;
        private Float averageStarRate;
        private String school;
        private String gender;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private Long id;
        private String loginId;
        private String name;
        private String nickName;
        private String profileImage;
        private String phoneNumber;
        private String email;
        private String status;
        private Float averageStarRate;
        private String school;
        private String age;
        private String gender;
        private List<AccountDto> accountDtoList;
    }

    @Data
    @Builder
    public static class updateDto {
        private String profileImage;
        private String phoneNumber;
        private String age;
        private String email;
        private String gender;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class updateNickName {
        private String nickName;
    }

    @Data
    @Builder
    public static class deleteDto {
        private String name;
        private String email;
    }


    @Data
    @Builder
    public static class AccountResponse {
        private String name;
        private String nickName;
        private String email;
        private List<AccountDto> accountDtoList;
    }
}
