package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.config.jwtConfig.JwtTokenGenerator;
import com.moyeota.moyeotaproject.controller.dto.TokenInfoDto;
import com.moyeota.moyeotaproject.domain.oAuth.OAuth;
import com.moyeota.moyeotaproject.domain.oAuth.OAuthProvider;
import com.moyeota.moyeotaproject.domain.oAuth.OAuthRepository;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;
import com.moyeota.moyeotaproject.component.OAuth.OAuthInfoResponse.OAuthInfoResponse;
import com.moyeota.moyeotaproject.component.OAuth.OAuthLoginParams.OAuthLoginParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthLoginService {

    private final UsersRepository usersRepository;
    private final OAuthRepository oAuthRepository;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final PasswordEncoder passwordEncoder;

    public TokenInfoDto login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        Long userId = findOrCreateMember(oAuthInfoResponse);
        Users user = usersRepository.findById(userId).orElseThrow(()
                -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));
        return jwtTokenGenerator.generate(user.getId());
    }

    private Long findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
        String oAuthProvider = oAuthInfoResponse.getOAuthProvider().name();
        String userEmail = oAuthInfoResponse.getEmail();

        // 이메일이 없는 경우 또는 소셜로그인 이름으로 조회
        Optional<OAuth> oAuthEntity = userEmail == null ?
                Optional.empty() :
                oAuthRepository.findByEmailAndName(userEmail, oAuthProvider);

        if (oAuthEntity.isPresent()) {
            Users user = oAuthEntity.get().getUser();
            return user.getId();
        } else {
            return newMember(oAuthInfoResponse);
        }
    }

    private Long newMember(OAuthInfoResponse oAuthInfoResponse) {
        String email = "";
        if (oAuthInfoResponse.getEmail() == null && oAuthInfoResponse.getOAuthProvider().name().equals("KAKAO")) {
            email = "@kakao.com";
        } else {
            email = oAuthInfoResponse.getEmail();
        }
        Users user = Users.builder()
                .email(email)
                .name(oAuthInfoResponse.getUsername())
                .gender(genderStringToBoolean(oAuthInfoResponse.getGender())) // TODO: 의논해서 바꾸는 것도 좋아보임(Entity에서 String으로 변경)
                .profileImage(oAuthInfoResponse.getProfileImage())
                .age(oAuthInfoResponse.getAge())
                .loginId(oAuthInfoResponse.getOAuthProvider().name() + " " + oAuthInfoResponse.getEmail()) // Kakao tae77777@naver.com
                .password(passwordEncoder.encode(oAuthInfoResponse.getOAuthProvider().name())) // 소셜로그인 정보로 인코딩
                .phoneNumber(oAuthInfoResponse.getPhoneNumber())
                .build();
        OAuth oAuth = OAuth.builder()
                .name(oAuthInfoResponse.getOAuthProvider().name())
                .email(email)
                .user(user)
                .build();
        user.updateOAuth(oAuth);
        usersRepository.save(user);
        oAuthRepository.save(oAuth);
        return user.getId();
    }

    // TODO: 논의 후 변경!
    private Boolean genderStringToBoolean(String gender) {
        if (gender == null) {
            return null;
        } else if (gender.equals("F")) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }
}
