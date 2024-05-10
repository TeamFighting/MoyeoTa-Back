package com.moyeota.moyeotaproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeota.moyeotaproject.config.jwtconfig.JwtTokenGenerator;
import com.moyeota.moyeotaproject.config.jwtconfig.JwtTokenProvider;
import com.moyeota.moyeotaproject.config.response.ResponseDto;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;
import com.moyeota.moyeotaproject.dto.UsersDto.TokenInfoDto;
import com.moyeota.moyeotaproject.dto.UsersDto.UserDto;
import com.moyeota.moyeotaproject.dto.UsersDto.UsersResponseDto;
import com.moyeota.moyeotaproject.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
class UsersControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UsersRepository usersRepository;

    @Autowired
    private UsersService usersService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    private String accessToken;

    @BeforeEach
    void beforeEach() {
        Long userId = 1L;
        TokenInfoDto generate = jwtTokenGenerator.generate(userId);
        accessToken = "Bearer " + generate.getAccessToken();

        // 테스트를 위한 가상의 사용자 저장
        Users userEntity = Users.builder()
                .id(userId)
                .name("테스트사용자")
                .email("tae77777@naver.com")
                .age("24")
                .loginId("moyeota")
                .password("moyeota")
                .build();
        when(usersRepository.findById(userId)).thenReturn(Optional.of(userEntity));
    }

    @Test
    void 사용자_정보_조회() throws Exception {
        // given
        MvcResult result = mockMvc.perform(get("/api/users")
                        .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andReturn();

        // when
        String content = result.getResponse().getContentAsString();
        TypeReference<ResponseDto<UserDto.Response>> typeReference = new TypeReference<>() {};
        ResponseDto<UserDto.Response> userResponse = objectMapper.readValue(content, typeReference);
        String message = userResponse.getMessage();
        UserDto.Response responseData = userResponse.getData();

        // then
        assertThat(message).isEqualTo("사용자 정보를 받아왔습니다.");
        assertThat(responseData.getId()).isEqualTo(1L);
        assertThat(responseData.getName()).isEqualTo("테스트사용자");
        assertThat(responseData.getEmail()).isEqualTo("tae77777@naver.com");
    }

    @Test
    void 사용자_정보_수정() throws Exception {
        // given
        // 변경 가능한 정보 : 프로필이미지, 핸드폰번호, 이메일, 성별
        String newProfileImage = "새로운이미지_URL";
        String newPhoneNumber = "새로운_번호";
        String newEmail = "moyeota@naver.com";
        String newGender = "FEMALE";

        // when
        MvcResult result = mockMvc.perform(put("/api/users/info")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(UserDto.updateDto.builder()
                                .profileImage(newProfileImage)
                                .phoneNumber(newPhoneNumber)
                                .email(newEmail)
                                .gender(newGender)
                                .build())))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        TypeReference<ResponseDto<UsersResponseDto>> typeReference = new TypeReference<>() {};
        ResponseDto<UsersResponseDto> userResponse = objectMapper.readValue(content, typeReference);
        String message = userResponse.getMessage();
        UsersResponseDto responseData = userResponse.getData();

        // then
        assertThat(message).isEqualTo("프로필 업데이트를 완료하였습니다");
        // 변경 불가능한 정보 : 이름
        assertThat(responseData.getId()).isEqualTo(1L);
        assertThat(responseData.getName()).isEqualTo("테스트사용자");
        assertThat(responseData.getAge()).isEqualTo("24");

        // 변경 가능한 정보
        assertThat(responseData.getProfileImage()).isEqualTo(newProfileImage);
        assertThat(responseData.getPhoneNumber()).isEqualTo(newPhoneNumber);
        assertThat(responseData.getGender()).isEqualTo(newGender);
    }

    @Test
    void 닉네임_생성() throws Exception {
        // given
        String nickName = "닉네임";

        // when
        MvcResult result = mockMvc.perform(post("/api/users/nickname")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(UserDto.updateNickName.builder()
                                .nickName(nickName)
                                .build())))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        TypeReference<ResponseDto<UserDto.updateNickName>> typeReference = new TypeReference<>() {};
        ResponseDto<UserDto.updateNickName> userResponse = objectMapper.readValue(content, typeReference);
        String message = userResponse.getMessage();
        UserDto.updateNickName responseData = userResponse.getData();

        // then
        assertThat(message).isEqualTo("닉네임이 생성되었습니다.");
        assertThat(responseData.getNickName()).isEqualTo(nickName);
    }

    @Test
    void 닉네임_수정() throws Exception {
        // given
        String newNickName = "새로운_닉네임";

        // when
        MvcResult result = mockMvc.perform(put("/api/users/nickname")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(UserDto.updateNickName.builder()
                                .nickName(newNickName)
                                .build())))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        TypeReference<ResponseDto<UserDto.updateNickName>> typeReference = new TypeReference<>() {};
        ResponseDto<UserDto.updateNickName> userResponse = objectMapper.readValue(content, typeReference);
        String message = userResponse.getMessage();
        UserDto.updateNickName responseData = userResponse.getData();

        // then
        assertThat(message).isEqualTo("닉네임이 변경되었습니다.");
        assertThat(responseData.getNickName()).isEqualTo(newNickName);
    }

    @Test
    void 학교_인증 () {
        // given

        // when

        // then
    }

    @Test
    void 학교_인증_코드_확인() {

    }
}