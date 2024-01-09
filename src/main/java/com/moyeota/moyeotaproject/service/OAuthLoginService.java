package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.config.exception.ApiException;
import com.moyeota.moyeotaproject.config.exception.ErrorCode;
import com.moyeota.moyeotaproject.config.jwtConfig.JwtTokenGenerator;
import com.moyeota.moyeotaproject.controller.dto.OAuthEmailDto;
import com.moyeota.moyeotaproject.controller.dto.TokenInfoDto;
import com.moyeota.moyeotaproject.controller.dto.UsersDto;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
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
        if (userEmail == null) {
            throw new ApiException(ErrorCode.NO_EMAIL_ERROR);
        }

        Optional<OAuth> oAuthEntity = oAuthRepository.findByEmailAndName(userEmail, oAuthProvider);

        if (oAuthEntity.isPresent()) {
            Users user = oAuthEntity.get().getUser();
            return user.getId();
        } else {
            return newMember(oAuthInfoResponse);
        }
    }

    private Long newMember(OAuthInfoResponse oAuthInfoResponse) {
        String email = oAuthInfoResponse.getEmail();
        Users user = Users.builder()
                .email(email)
                .name(oAuthInfoResponse.getUsername())
                .gender(oAuthInfoResponse.getGender())
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

    public Long signup(OAuthEmailDto oAuthEmailDto) {
        Users user = Users.builder()
                .loginId(oAuthEmailDto.getOauth() + " " + oAuthEmailDto.getEmail())
                .password(passwordEncoder.encode(oAuthEmailDto.getOauth()))
                .email(oAuthEmailDto.getEmail())
                .build();
        usersRepository.save(user);
        OAuth oAuth = OAuth.builder()
                .name(oAuthEmailDto.getOauth())
                .email(oAuthEmailDto.getEmail())
                .user(user)
                .build();
        oAuthRepository.save(oAuth);
        return user.getId();
    }
}
