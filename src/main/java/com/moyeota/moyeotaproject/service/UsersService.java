package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.config.jwtConfig.JwtTokenGenerator;
import com.moyeota.moyeotaproject.config.jwtConfig.JwtTokenProvider;
import com.moyeota.moyeotaproject.controller.dto.LoginResponseDto;
import com.moyeota.moyeotaproject.controller.dto.SchoolRequestDto;
import com.moyeota.moyeotaproject.controller.dto.SignUpRequestDto;
import com.moyeota.moyeotaproject.domain.users.Entity.Users;
import com.moyeota.moyeotaproject.domain.users.Entity.UsersRepository;
import com.univcert.api.UnivCert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${univcert.api.key}")
    String apiKey;

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


    public Users addInfo(String authorization, SignUpRequestDto signUpRequestDto) {
        Users users = getUserByToken(authorization);
        return users;
    }

    public Users getUserByToken(String accessToken) {
        Optional<Users> users = usersRepository.findById(jwtTokenProvider.extractSubjectFromJwt(accessToken));
        if (users.isPresent()) {
            return users.get();
        } else {
            throw new RuntimeException("토큰에 해당하는 멤버가 없습니다.");
        }
    }

    public String schoolEmail(SchoolRequestDto schoolRequestDto) throws IOException {
        return UnivCert.certify(apiKey, schoolRequestDto.getEmail(), schoolRequestDto.getUnivName(), schoolRequestDto.isUniv_check()).toString();
    }

    public String schoolEmailCheck(SchoolRequestDto schoolRequestDto) throws IOException {
        return UnivCert.certifyCode(apiKey, schoolRequestDto.getEmail(), schoolRequestDto.getUnivName(), schoolRequestDto.getCode()).toString();
    }
}
