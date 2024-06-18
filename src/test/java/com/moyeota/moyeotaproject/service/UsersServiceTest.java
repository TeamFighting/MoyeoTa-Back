//package com.moyeota.moyeotaproject.service;
//
//import com.moyeota.moyeotaproject.config.jwtConfig.JwtTokenGenerator;
//import com.moyeota.moyeotaproject.domain.account.Account;
//import com.moyeota.moyeotaproject.domain.account.AccountRepository;
//import com.moyeota.moyeotaproject.domain.users.Users;
//import com.moyeota.moyeotaproject.domain.users.UsersRepository;
//import com.moyeota.moyeotaproject.dto.UsersDto.AccountDto;
//import com.moyeota.moyeotaproject.dto.UsersDto.TokenInfoDto;
//import com.moyeota.moyeotaproject.dto.UsersDto.UserDto;
//import com.moyeota.moyeotaproject.dto.UsersDto.UsersResponseDto;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@Slf4j
//@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
//@AutoConfigureMockMvc
//@SpringBootTest
//class UsersServiceTest {
//
//    @MockBean
//    private UsersRepository usersRepository;
//
//    @MockBean
//    private AccountRepository accountRepository;
//
//    @Autowired
//    private UsersService usersService;
//
//    @Autowired
//    private JwtTokenGenerator jwtTokenGenerator;
//
//    private Users userEntity;
//
//    @BeforeEach
//    void beforeEach() {
//        userEntity = Users.builder()
//                .id(1L)
//                .name("테스트사용자")
//                .profileImage("https://ssl.pstatic.net/static/pwe/address/img_profile.png")
//                .phoneNumber("010-7757-6340")
//                .email("tae77777@naver.com")
//                .loginId("moyeota")
//                .password("moyeota")
//                .status("GOOGLE")
//                .gender("MALE")
//                .averageStarRate(Float.valueOf("3.2"))
//                .school("건국대학교")
//                .isAuthenticated(false)
//                .age("24")
//                .build();
//        when(usersRepository.findById(userEntity.getId())).thenReturn(Optional.ofNullable(userEntity));
//        when(usersRepository.save(userEntity)).thenReturn(userEntity);
//    }
//
//    @Test
//    public void 토큰으로_유저_조회_테스트() {
//        // given
//        TokenInfoDto generate = jwtTokenGenerator.generate(userEntity.getId());
//        String accessToken = "Bearer " + generate.getAccessToken();
//
//        // when
//        Users userByToken = usersService.getUserByToken(accessToken);
//
//        // then
//        assertThat(userByToken.getId()).isEqualTo(1L);
//        assertThat(userByToken.getEmail()).isEqualTo("tae77777@naver.com");
//    }
//
//    @Test
//    public void 유저_정보_조회() {
//        // given
//        TokenInfoDto generate = jwtTokenGenerator.generate(userEntity.getId());
//        String accessToken = "Bearer " + generate.getAccessToken();
//
//        // when
//        UserDto.Response users = usersService.getInfo(accessToken);
//
//        // then
//        assertThat(users.getName()).isEqualTo("테스트사용자");
//        assertThat(users.getProfileImage()).isEqualTo("https://ssl.pstatic.net/static/pwe/address/img_profile.png");
//        assertThat(users.getPhoneNumber()).isEqualTo("010-7757-6340");
//        assertThat(users.getEmail()).isEqualTo("tae77777@naver.com");
//        assertThat(users.getLoginId()).isEqualTo("moyeota");
//        assertThat(users.getStatus()).isEqualTo("GOOGLE");
//        assertThat(users.getGender()).isEqualTo("MALE");
//        assertThat(users.getAverageStarRate()).isEqualTo(3.2f);
//        assertThat(users.getSchool()).isEqualTo("건국대학교");
//        assertThat(users.getAge()).isEqualTo("24");
//    }
//
//    @Test
//    public void 유저_닉네임_생성() {
//        // given
//        TokenInfoDto generate = jwtTokenGenerator.generate(userEntity.getId());
//        String accessToken = "Bearer " + generate.getAccessToken();
//
//        // when
//        UsersResponseDto usersWithNickName = usersService.createNickName(accessToken, "첫 닉네임");
//
//        // then
//        assertThat(usersWithNickName.getNickName()).isEqualTo("첫 닉네임");
//    }
//
//
//    @Test
//    public void 유저_닉네임_수정() {
//        // given
//        TokenInfoDto generate = jwtTokenGenerator.generate(userEntity.getId());
//        String accessToken = "Bearer " + generate.getAccessToken();
//        String newNickName = "모여타중독자";
//
//        // when
//        UsersResponseDto usersResponseDto = usersService.updateNickName(accessToken, newNickName);
//
//        // then
//        assertThat(usersResponseDto.getNickName()).isEqualTo("모여타중독자");
//    }
//
//    @Test
//    public void 유저_계좌_추가() {
//        // given
//        TokenInfoDto generate = jwtTokenGenerator.generate(userEntity.getId());
//        String accessToken = "Bearer " + generate.getAccessToken();
//        String bankName = "토스";
//        String accountNumber = "1000-900231415";
//
//        // when
//        when(accountRepository.save(any())).thenReturn(Account.builder().bankName(bankName).accountNumber(accountNumber).build());
//        UserDto.AccountResponse accountResponse = usersService.addAccount(accessToken, new AccountDto(bankName, accountNumber));
//
//        // then
//        assertThat(accountResponse.getAccountDtoList()).extracting("bankName").contains(bankName);
//        assertThat(accountResponse.getAccountDtoList()).extracting("accountNumber").contains(accountNumber);
//    }
//
//    @Test
//    public void 계좌_추가_후_유저_조회시_계좌_조회() {
//        // given
//        TokenInfoDto generate = jwtTokenGenerator.generate(userEntity.getId());
//        String accessToken = "Bearer " + generate.getAccessToken();
//        String bankName = "토스";
//        String accountNumber = "1000-900231415";
//
//        // when
//        when(accountRepository.save(any())).thenReturn(Account.builder().bankName(bankName).accountNumber(accountNumber).build());
//        UserDto.AccountResponse accountResponse = usersService.addAccount(accessToken, new AccountDto(bankName, accountNumber));
//        List<AccountDto> accountDtoList = usersService.getInfo(accessToken).getAccountDtoList();
//
//        // then
//        assertThat(accountDtoList).extracting("bankName").contains(bankName);
//        assertThat(accountDtoList).extracting("accountNumber").contains(accountNumber);
//    }
//
//
//}