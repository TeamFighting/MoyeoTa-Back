package com.moyeota.moyeotaproject.dto.UsersDto;

import lombok.Data;

@Data
public class UserVerifyRequest {
    private String verifyCode;
    private String phoneNumber;
    private String carrierDomains; // 통신사
}
