package com.moyeota.moyeotaproject.controller.dto;

import com.moyeota.moyeotaproject.domain.users.OAuth.OAuthProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {

    private String name;
    private String profileImage;
    private String phoneNumber;
    private String email;
    private String loginId;
    private String password;
    private String status;
    private Boolean gender;
    private Float averageStarRate;
    private String school;
    private Boolean isAuthenticated;
    private OAuthProvider provider;
}
