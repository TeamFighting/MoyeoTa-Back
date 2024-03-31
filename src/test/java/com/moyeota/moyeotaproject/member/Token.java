package com.moyeota.moyeotaproject.member;

import com.moyeota.moyeotaproject.config.jwtconfig.JwtTokenGenerator;
import com.moyeota.moyeotaproject.config.jwtconfig.JwtTokenProvider;
import com.moyeota.moyeotaproject.dto.UsersDto.TokenInfoDto;
import org.junit.jupiter.api.Test;

import java.util.Date;

@SuppressWarnings("NonAsciiCharacters")
public class Token {
    // TODO: test yml에 따로 저장하기
    String salt = "RlaXoVBsYt9V7zq57TejMnVUyzblYcfPQye08f7MGVA9XkHa";

    public JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(salt);
    public JwtTokenGenerator jwtTokenGenerator = new JwtTokenGenerator(jwtTokenProvider);

    @Test
    void 토큰_만들기() {
        Long userId = 1L;
        jwtTokenProvider.generateToken(userId.toString(),
                new Date(1000 * 60 * 60 * 24 * 21));
        TokenInfoDto generate = jwtTokenGenerator.generate(1L);
        System.out.println(generate);
    }
}
