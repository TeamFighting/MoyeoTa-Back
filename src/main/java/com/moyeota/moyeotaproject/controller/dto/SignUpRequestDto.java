package com.moyeota.moyeotaproject.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequestDto {

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
