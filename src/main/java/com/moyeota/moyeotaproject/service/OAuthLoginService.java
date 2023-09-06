package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.config.jwtConfig.JwtTokenGenerator;
import com.moyeota.moyeotaproject.controller.dto.TokenInfoDto;
import com.moyeota.moyeotaproject.domain.users.Entity.Users;
import com.moyeota.moyeotaproject.domain.users.Entity.UsersRepository;
import com.moyeota.moyeotaproject.domain.users.OAuth.OAuthInfoResponse.OAuthInfoResponse;
import com.moyeota.moyeotaproject.domain.users.OAuth.OAuthLoginParams.OAuthLoginParams;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {

    private final UsersRepository usersRepository;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final PasswordEncoder passwordEncoder;

    public TokenInfoDto login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        Long memberId = findOrCreateMember(oAuthInfoResponse);
        Users user = usersRepository.findById(memberId).get();
        return jwtTokenGenerator.generate(user.getId());
    }

    private Long findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
        Optional<Users> user = usersRepository.findByEmail(oAuthInfoResponse.getEmail());
        if (user.isPresent()) {
            return user.get().getId();
        } else{
            return newMember(oAuthInfoResponse);
        }
    }

    private Long newMember(OAuthInfoResponse oAuthInfoResponse) {
        Users member = Users.builder()
                .email(oAuthInfoResponse.getEmail())
                .name(oAuthInfoResponse.getUsername())
                .gender(genderStringToBoolean(oAuthInfoResponse.getGender()))
                .profileImage(oAuthInfoResponse.getProfileImage())
                .loginId(oAuthInfoResponse.getEmail())
                .password(passwordEncoder.encode(oAuthInfoResponse.getOAuthProvider().name())) // 소셜로그인 정보로 인코딩
                .provider(oAuthInfoResponse.getOAuthProvider())
                .build();

        return usersRepository.save(member).getId();
    }

    private Boolean genderStringToBoolean(String gender){
        if(gender.equals("F")){
            return Boolean.FALSE;
        } else{
            return Boolean.TRUE;
        }
    }
}
