package com.moyeota.moyeotaproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeota.moyeotaproject.controller.dto.OAuth2Attribute;
import com.moyeota.moyeotaproject.controller.dto.SignUpRequestDto;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    // 자체 서버 회원가입 -> 필요없으면 나중에 삭제
    public void signup(SignUpRequestDto signUpRequestDto) {
        final boolean isExistLoginId = usersRepository.existsByLoginId(signUpRequestDto.getLoginId());
        if (isExistLoginId) {
            throw new RuntimeException("아이디 중복입니다");
        } else {
            Users createUser = Users.builder()
                    .loginId(signUpRequestDto.getLoginId())
                    .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                    .name(signUpRequestDto.getName())
                    .profileImage(signUpRequestDto.getProfileImage())
                    .phoneNumber(signUpRequestDto.getPhoneNumber())
                    .email(signUpRequestDto.getEmail())
                    .status(signUpRequestDto.getStatus())
                    .gender(signUpRequestDto.getGender())
                    .averageStarRate(signUpRequestDto.getAverageStarRate())
                    .school(signUpRequestDto.getSchool())
                    .isAuthenticated(false)
                    .build();
            usersRepository.save(createUser);
        }
    }



    // 소셜 회원가입
    public void oauth2Signup(String oauth2AccessToken, String socialLogin) throws ParseException, JsonProcessingException {
        if(socialLogin.equals("Google")){
            OAuth2Attribute auth2Attribute = getGoogleData(oauth2AccessToken);
            Users createdUser = Users.builder()
                    .loginId(auth2Attribute.getUserId())
                    .password(passwordEncoder.encode("google")) // 그냥 구글 해시하여 저장했습니다.
                    .name(auth2Attribute.getUsername())
                    .profileImage(auth2Attribute.getPicture())
                    .email(auth2Attribute.getEmail())
                    .isAuthenticated(false)
                    .build();
            usersRepository.save(createdUser);
        } else if(socialLogin.equals("Kakao")){
            // OAuth2Attribute auth2Attribute = getKakaoData(oauth2AccessToken);
        }
    }

    // 소셜 로그인
//    public void oauth2Login(String oauth2AccessToken, String socialLogin) {
//        Map<String, Object> memberMap = oAuth2UserService.findOrSaveMember(idToken, "google");
//        TokenDTO tokenDTO = tokenService.createToken((MemberDTO) memberMap.get("dto"));
//
//        ResponseCookie responseCookie = ResponseCookie
//                .from("refresh_token", tokenDTO.getRefreshToken())
//                .httpOnly(true)
//                .secure(true)
//                .sameSite("None")
//                .maxAge(tokenDTO.getDuration())
//                .path("/")
//                .build();
//
//        TokenResponseDTO tokenResponseDTO = TokenResponseDTO.builder()
//                .isNewMember(false)
//                .accessToken(tokenDTO.getAccessToken())
//                .build();
//    }

    // 구글로부터 정보 받아오기
    private OAuth2Attribute getGoogleData(String id_token) throws JsonProcessingException, ParseException {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String googleApi = "https://oauth2.googleapis.com/tokeninfo";
        String targetUrl = UriComponentsBuilder.fromHttpUrl(googleApi).queryParam("id_token", id_token).build().toUriString();

        ResponseEntity<String> response = restTemplate.exchange(targetUrl, HttpMethod.GET, entity, String.class);

        JSONParser parser = new JSONParser();
        JSONObject jsonBody = (JSONObject) parser.parse(response.getBody());

        Map<String, Object> body = new ObjectMapper().readValue(jsonBody.toString(), Map.class);

        return OAuth2Attribute.of("google", "sub", body);
    }

    // 네이버로부터 정보 받아오기
    private OAuth2Attribute getNaverData(String id_token) throws JsonProcessingException, ParseException {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String googleApi = "https://oauth2.googleapis.com/tokeninfo";
        String targetUrl = UriComponentsBuilder.fromHttpUrl(googleApi).queryParam("id_token", id_token).build().toUriString();

        ResponseEntity<String> response = restTemplate.exchange(targetUrl, HttpMethod.GET, entity, String.class);

        JSONParser parser = new JSONParser();
        JSONObject jsonBody = (JSONObject) parser.parse(response.getBody());

        Map<String, Object> body = new ObjectMapper().readValue(jsonBody.toString(), Map.class);

        return OAuth2Attribute.of("google", "sub", body);
    }
}
