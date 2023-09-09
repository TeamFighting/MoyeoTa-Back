package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.config.jwtConfig.JwtTokenProvider;
import com.moyeota.moyeotaproject.controller.dto.SchoolDto;
import com.moyeota.moyeotaproject.controller.dto.SignUpDto;
import com.moyeota.moyeotaproject.domain.users.Entity.Users;
import com.moyeota.moyeotaproject.domain.users.Entity.UsersRepository;
import com.univcert.api.UnivCert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public Users addInfo(String authorization, SignUpDto.Request signUpDto) {
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

    public String schoolEmail(SchoolDto.Request schoolDto) throws IOException {
        return UnivCert.certify(apiKey, schoolDto.getEmail(), schoolDto.getUnivName(), schoolDto.isUniv_check()).toString();
    }

    public String schoolEmailCheck(SchoolDto.Request schoolDto) throws IOException {
        return UnivCert.certifyCode(apiKey, schoolDto.getEmail(), schoolDto.getUnivName(), schoolDto.getCode()).toString();
    }
}
