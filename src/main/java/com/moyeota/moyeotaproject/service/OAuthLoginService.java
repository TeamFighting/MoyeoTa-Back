package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.config.exception.ApiException;
import com.moyeota.moyeotaproject.config.exception.ErrorCode;
import com.moyeota.moyeotaproject.config.jwtConfig.JwtTokenGenerator;
import com.moyeota.moyeotaproject.controller.dto.TokenInfoDto;
import com.moyeota.moyeotaproject.domain.oAuth.OAuth;
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
        Long userId = findOrCreateUserAndOAuth(oAuthInfoResponse);
        Users user = getUserById(userId);
        return jwtTokenGenerator.generate(user.getId());
    }

    private Long findOrCreateUserAndOAuth(OAuthInfoResponse oAuthInfoResponse) {
        String oAuthProvider = oAuthInfoResponse.getOAuthProvider().name();
        String userEmail = oAuthInfoResponse.getEmail();
        if (userEmail == null) {
            throw new ApiException(ErrorCode.NO_EMAIL_ERROR);
        }

        Optional<OAuth> oAuthEntity = oAuthRepository.findByEmailAndName(userEmail, oAuthProvider);

        return oAuthEntity.map(oAuth -> oAuth.getUser().getId())
                .orElseGet(() -> createUserAndOAuth(oAuthInfoResponse));
    }

    private Long createUserAndOAuth(OAuthInfoResponse oAuthInfoResponse) {
        Users user = createUserFromOAuthInfo(oAuthInfoResponse);
        OAuth oAuth = createOAuthFromOAuthInfo(user, oAuthInfoResponse);
        saveUserAndOAuth(user, oAuth);
        return user.getId();
    }

    private Users createUserFromOAuthInfo(OAuthInfoResponse oAuthInfoResponse) {
        return Users.builder()
                .email(oAuthInfoResponse.getEmail())
                .name(oAuthInfoResponse.getUsername())
                .gender(oAuthInfoResponse.getGender())
                .profileImage(oAuthInfoResponse.getProfileImage())
                .age(oAuthInfoResponse.getAge())
                .loginId(oAuthInfoResponse.getOAuthProvider().name() + " " + oAuthInfoResponse.getEmail())
                .password(passwordEncoder.encode(oAuthInfoResponse.getOAuthProvider().name()))
                .phoneNumber(oAuthInfoResponse.getPhoneNumber())
                .build();
    }

    private OAuth createOAuthFromOAuthInfo(Users user, OAuthInfoResponse oAuthInfoResponse) {
        return OAuth.builder()
                .name(oAuthInfoResponse.getOAuthProvider().name())
                .email(oAuthInfoResponse.getEmail())
                .user(user)
                .build();
    }

    private void saveUserAndOAuth(Users user, OAuth oAuth) {
        usersRepository.save(user);
        oAuthRepository.save(oAuth);
    }

    private Users getUserById(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));
    }

}
