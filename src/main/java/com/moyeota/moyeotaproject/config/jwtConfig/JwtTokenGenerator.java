package com.moyeota.moyeotaproject.config.jwtConfig;

import com.moyeota.moyeotaproject.controller.dto.TokenInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Date;

//@Component
@Configuration
@RequiredArgsConstructor
public class JwtTokenGenerator {

    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 21;   // 21일
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 21;  // 21일

    private final JwtTokenProvider jwtTokenProvider;

    public TokenInfoDto generate(Long userId) {
        long now = (new Date()).getTime();
        Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiredAt = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        String subject = userId.toString();
        String accessToken = jwtTokenProvider.generateToken(subject, accessTokenExpiredAt);
        String refreshToken = jwtTokenProvider.generateToken(subject, refreshTokenExpiredAt);

        return TokenInfoDto.of(accessToken, refreshToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME / 1000L);
    }
}
