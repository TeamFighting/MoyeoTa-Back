package com.moyeota.moyeotaproject.controller;

import com.moyeota.moyeotaproject.config.jwtconfig.JwtTokenGenerator;
import com.moyeota.moyeotaproject.config.jwtconfig.JwtTokenProvider;
import com.moyeota.moyeotaproject.dto.UsersDto.TokenInfoDto;
import com.moyeota.moyeotaproject.dto.UsersDto.UserDto;
import com.moyeota.moyeotaproject.service.UsersService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
class UsersControllerTest {

    @Autowired
    private UsersService usersService;

    String salt = "RlaXoVBsYt9V7zq57TejMnVUyzblYcfPQye08f7MGVA9XkHa";

    public JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(salt);
    public JwtTokenGenerator jwtTokenGenerator = new JwtTokenGenerator(jwtTokenProvider);

    private String accessToken;

    @BeforeEach
    void 토큰_만들기() {
        Long userId = 1L;
        jwtTokenProvider.generateToken(userId.toString(),
                new Date(1000 * 60 * 60 * 24 * 21));
        TokenInfoDto generate = jwtTokenGenerator.generate(1L);
        System.out.println(generate);
        System.out.println(generate.getAccessToken());
        accessToken = generate.getAccessToken();
    }

    @Test
    @DisplayName("사용자 정보 조회")
    void 사용자_정보_조회() {
        UserDto.Response info = usersService.getInfo("Bearer " + accessToken);
        assertThat(info.getId()).isEqualTo(1L);
    }

    @Test
    void updateInfo() {
    }

    @Test
    void schoolEmail() {
    }

    @Test
    void schoolEmailCheck() {
    }

    @Test
    void createNickName() {
    }

    @Test
    void updateNickname() {
    }

    @Test
    void setProfileImageDefault() {
    }

    @Test
    void updateProfileImage() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void addAccount() {
    }
}