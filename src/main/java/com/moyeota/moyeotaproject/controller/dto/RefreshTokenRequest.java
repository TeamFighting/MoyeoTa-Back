package com.moyeota.moyeotaproject.controller.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RefreshTokenRequest {

    private final String accessToken;
    private final String refreshToken;
}
