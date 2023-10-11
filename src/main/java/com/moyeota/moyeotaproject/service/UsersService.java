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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
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

    public String schoolEmail(String accessToken, SchoolDto.RequestForUnivCode schoolDto) throws IOException {
        usersRepository.findById(jwtTokenProvider.extractSubjectFromJwt(accessToken)).orElseThrow(()
                -> new RuntimeException("해당하는 유저가 없습니다."));
        Map<String, Object> objectMap = UnivCert.certify(apiKey, schoolDto.getEmail(), schoolDto.getUnivName(), true);
        String success = objectMap.get("success").toString();
        if (success.equals("false")) {
            String message = objectMap.get("message").toString();
            throw new RuntimeException(message);
        }
        return schoolDto.getEmail();
    }

    public SchoolDto.ResponseSuccess schoolEmailCheck(String accessToken, SchoolDto.RequestForUnivCodeCheck schoolDto) throws IOException {
        Users users = usersRepository.findById(jwtTokenProvider.extractSubjectFromJwt(accessToken)).orElseThrow(()
                -> new RuntimeException("해당하는 유저가 없습니다."));
        Map<String, Object> objectMap = UnivCert.certifyCode(apiKey, schoolDto.getEmail(), schoolDto.getUnivName(), schoolDto.getCode());
        if(objectMap.get("success").toString().equals("false")){
            String message = objectMap.get("message").toString();
            throw new RuntimeException(message);
        }
        // 유저 학교 인증
        users.updateSchoolAuthenticate(schoolDto.getUnivName());
        return SchoolDto.ResponseSuccess.builder()
                .univName(objectMap.get("univName").toString())
                .certified_email(objectMap.get("certified_email").toString())
                .certified_date(objectMap.get("certified_date").toString())
                .build();
    }
    public String schoolEmailReset(String accessToken, SchoolDto.RequestForUnivCode schoolDto) throws IOException {
        UnivCert.clear(apiKey);
        return schoolEmail(accessToken, schoolDto);
    }

    public String findNameByUserId(Long userId) {
        return usersRepository.findNameByUserId(userId);
    }
}
