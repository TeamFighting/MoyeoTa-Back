package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.config.jwtConfig.JwtTokenProvider;
import com.moyeota.moyeotaproject.controller.dto.SchoolDto;
import com.moyeota.moyeotaproject.controller.dto.UsersDto;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;
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

    public UsersDto.Response addInfo(String authorization, UsersDto.updateDto usersDto) {
        Users users = getUserByToken(authorization);
        System.out.println("users = " + users);
        users.updateUsers(usersDto);
        UsersDto.Response updateDto = UsersDto.Response.builder()
                .loginId(users.getLoginId())
                .name(users.getName())
                .profileImage(users.getProfileImage())
                .email(users.getEmail())
                .status(users.getStatus())
                .averageStarRate(users.getAverageStarRate())
                .school(users.getSchool())
                .gender(users.getGender())
                .build();

        return updateDto;
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

    public String findNameByUserId(Long userId){
        return usersRepository.findNameByUserId(userId);
    }
}
