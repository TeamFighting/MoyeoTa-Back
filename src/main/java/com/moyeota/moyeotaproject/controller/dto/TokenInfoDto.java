package com.moyeota.moyeotaproject.controller.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Data
public class TokenInfoDto {

    private final String grantType;
    private final String accessToken;
}
