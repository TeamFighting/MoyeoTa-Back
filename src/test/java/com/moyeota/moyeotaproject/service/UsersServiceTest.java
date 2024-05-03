package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.config.jwtconfig.JwtTokenGenerator;
import com.moyeota.moyeotaproject.config.jwtconfig.JwtTokenProvider;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;
import com.moyeota.moyeotaproject.dto.UsersDto.TokenInfoDto;
import com.moyeota.moyeotaproject.dto.UsersDto.UserDto;
import com.moyeota.moyeotaproject.dto.UsersDto.UsersResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
class UsersServiceTest {

    @Autowired
    PlatformTransactionManager transactionManager;

    TransactionStatus status;

    String salt = "RlaXoVBsYt9V7zq57TejMnVUyzblYcfPQye08f7MGVA9XkHa";

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UsersService usersService;

    public JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(salt);
    public JwtTokenGenerator jwtTokenGenerator = new JwtTokenGenerator(jwtTokenProvider);

    private String accessToken;

    @BeforeEach
    void beforeEach() {
        // 트랜잭션 시작
        status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        Long userId = 1L;
        jwtTokenProvider.generateToken(userId.toString(),
                new Date(1000 * 60 * 60 * 24 * 21));
        TokenInfoDto generate = jwtTokenGenerator.generate(1L);
        System.out.println(generate);
        System.out.println(generate.getAccessToken());
        accessToken = "Bearer " + generate.getAccessToken();

        Users users = Users.builder()
                .name("테스트사용자")
                .nickName("택시중독자")
                .profileImage("https://ssl.pstatic.net/static/pwe/address/img_profile.png")
                .phoneNumber("010-7757-6340")
                .email("tae77777@naver.com")
                .loginId("moyeota")
                .password("moyeota")
                .status("GOOGLE")
                .gender("MALE")
                .averageStarRate(Float.valueOf("3.2"))
                .school("건국대학교")
                .isAuthenticated(false)
                .age("24")
                .build();
        usersRepository.save(users);
    }

    @AfterEach
    void afterAll() {
        // 트랜잭션 롤백
        transactionManager.rollback(status);
    }

    @Test
    @Order(1)
    public void 토큰으로_유저_조회_테스트() {
        // when
        Users userByToken = usersService.getUserByToken(accessToken);

        // then
        assertThat(userByToken.getId()).isEqualTo(1L);
    }

    @Test
    public void 유저_정보_조회() {
        // when
        UserDto.Response users = usersService.getInfo(accessToken);

        // then
        assertThat(users.getName()).isEqualTo("테스트사용자");
        assertThat(users.getNickName()).isEqualTo("택시중독자");
        assertThat(users.getLoginId()).isEqualTo("moyeota");
        assertThat(users.getGender()).isEqualTo("MALE");
        assertThat(users.getSchool()).isEqualTo("건국대학교");
        assertThat(users.getAge()).isEqualTo("24");
    }

    @Test
    public void 유저_닉네임_수정() {
        // given
        String newNickName = "모여타중독자";
        UserDto.Response oldUser = usersService.getInfo(accessToken);

        // when
        assertThat(oldUser.getNickName()).isEqualTo("택시중독자");
        UsersResponseDto usersResponseDto = usersService.updateNickName(accessToken, newNickName);

        // then
        assertThat(usersResponseDto.getNickName()).isEqualTo("모여타중독자");
    }
}