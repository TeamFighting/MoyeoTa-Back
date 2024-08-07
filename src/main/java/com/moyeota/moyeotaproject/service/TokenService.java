package com.moyeota.moyeotaproject.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.moyeota.moyeotaproject.config.exception.ApiException;
import com.moyeota.moyeotaproject.config.exception.ErrorCode;
import com.moyeota.moyeotaproject.config.jwtConfig.JwtTokenGenerator;
import com.moyeota.moyeotaproject.config.jwtConfig.JwtTokenProvider;
import com.moyeota.moyeotaproject.dto.UsersDto.RefreshTokenRequest;
import com.moyeota.moyeotaproject.dto.UsersDto.TokenInfoDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenGenerator jwtTokenGenerator;

    public TokenInfoDto generateRefreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        Long usersId = jwtTokenProvider.extractSubjectFromJwt(refreshToken);
        log.info("userId = {}", usersId);
        return jwtTokenGenerator.generate(usersId);
    }
}
