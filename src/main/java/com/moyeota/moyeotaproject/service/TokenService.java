package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.config.jwtConfig.JwtTokenGenerator;
import com.moyeota.moyeotaproject.config.jwtConfig.JwtTokenProvider;
import com.moyeota.moyeotaproject.controller.dto.RefreshTokenRequest;
import com.moyeota.moyeotaproject.controller.dto.TokenInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenGenerator jwtTokenGenerator;

    public TokenInfoDto generateRefreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        Long usersId = jwtTokenProvider.extractSubjectFromJwt(refreshToken);
        return jwtTokenGenerator.generate(usersId);
    }
}
