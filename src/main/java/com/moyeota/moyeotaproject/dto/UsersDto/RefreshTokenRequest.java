package com.moyeota.moyeotaproject.dto.UsersDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RefreshTokenRequest {

    private final String accessToken;
    private final String refreshToken;
}
