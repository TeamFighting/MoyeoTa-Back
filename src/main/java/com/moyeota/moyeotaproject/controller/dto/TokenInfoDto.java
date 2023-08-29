package com.moyeota.moyeotaproject.controller.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class TokenInfoDto {

    private final String accessToken;
    private final String refreshToken;
    private final String grantType;
    private final Long expiresIn;

    public static TokenInfoDto of(String accessToken, String refreshToken, String grantType, Long expiresIn) {
        return new TokenInfoDto(accessToken, refreshToken, grantType, expiresIn);
    }
}
