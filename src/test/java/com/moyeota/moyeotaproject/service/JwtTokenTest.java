package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.config.jwtConfig.JwtTokenGenerator;
import com.moyeota.moyeotaproject.config.jwtConfig.JwtTokenProvider;
import com.moyeota.moyeotaproject.dto.UsersDto.TokenInfoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JwtTokenTest {

    @Autowired
    private JwtTokenGenerator tokenGenerator;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Test
    void JWT_토큰_생성_성공() {
        // given
        Long userId = 0L;

        // when
        TokenInfoDto tokenInfoDto = tokenGenerator.generate(userId);

        // then
        assertThat(tokenInfoDto.getGrantType()).isEqualTo("Bearer");
        assertThat(tokenInfoDto.getAccessToken()).isNotBlank();
        assertThat(tokenInfoDto.getRefreshToken()).isNotBlank();
        assertThat(tokenInfoDto.getExpiresIn()).isNotNull();
    }

    @Test
    void JWT_토큰_검증_성공() {
        // given
        Long userId = 0L;
        TokenInfoDto tokenInfoDto = tokenGenerator.generate(userId);
        String accessToken = "Bearer " + tokenInfoDto.getAccessToken();
        System.out.println("accessToken = " + accessToken);

        // when
        Long extractUserId = tokenProvider.extractSubjectFromJwt(accessToken);

        // then
        assertThat(extractUserId).isEqualTo(userId);
    }
}