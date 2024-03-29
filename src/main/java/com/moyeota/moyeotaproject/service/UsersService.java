package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.config.jwtconfig.JwtTokenProvider;
import com.moyeota.moyeotaproject.controller.dto.SchoolDto;
import com.moyeota.moyeotaproject.controller.dto.UserDto;
import com.moyeota.moyeotaproject.controller.dto.UsersDto.UsersResponseDto;
import com.moyeota.moyeotaproject.domain.oAuth.OAuth;
import com.moyeota.moyeotaproject.domain.oAuth.OAuthRepository;
import com.moyeota.moyeotaproject.domain.schoolEmail.SchoolEmail;
import com.moyeota.moyeotaproject.domain.schoolEmail.SchoolEmailRepository;
import com.moyeota.moyeotaproject.domain.schoolEmailRedis.SchoolEmailRedis;
import com.moyeota.moyeotaproject.domain.schoolEmailRedis.SchoolEmailRedisRepository;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UsersService {

    private final UsersRepository usersRepository;
    private final OAuthRepository oAuthRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JavaMailSender javaMailSender;
    private final SchoolEmailRepository schoolEmailRepository;
    private final SchoolEmailRedisRepository redisRepository;

    public Users autoRegister() {
        Users users = Users.builder()
                .loginId("5000")
                .password("1234")
                .email("example@naver.com")
                .build();
        return usersRepository.save(users);
    }

    @Transactional
    public UserDto.Response getInfo(String accessToken) {
        Users users = getUserByToken(accessToken);
        UserDto.Response usersDto = UserDto.Response.builder()
                .id(users.getId())
                .loginId(users.getLoginId())
                .name(users.getName())
                .nickName(users.getNickName())
                .profileImage(users.getProfileImage())
                .email(users.getEmail())
                .age(users.getAge())
                .status(users.getStatus())
                .averageStarRate(users.getAverageStarRate())
                .school(users.getSchool())
                .gender(users.getGender())
                .build();
        return usersDto;
    }

    @Transactional
    public UsersResponseDto addInfo(String accessToken, UserDto.updateDto usersDto) {
        Users users = getUserByToken(accessToken);
        users.updateUsers(usersDto);
        Optional<OAuth> oauthEntity = oAuthRepository.findByUserId(users.getId());
        oauthEntity.ifPresent(oAuth -> oAuth.updateEmail(usersDto.getEmail()));
        return UsersResponseDto.builder()
                .loginId(users.getLoginId())
                .name(users.getName())
                .nickName(users.getNickName())
                .profileImage(users.getProfileImage())
                .email(users.getEmail())
                .status(users.getStatus())
                .age(users.getAge())
                .averageStarRate(users.getAverageStarRate())
                .school(users.getSchool())
                .gender(users.getGender())
                .build();
    }

    @Transactional
    public UserDto.deleteDto deleteUser(String accessToken) {
        Users users = getUserByToken(accessToken);
        UserDto.deleteDto deleteDto = UserDto.deleteDto.builder()
                .name(users.getName())
                .email(users.getEmail()).build();
        usersRepository.delete(users);
        return deleteDto;
    }

    public Users getUserByToken(String accessToken) {
        Optional<Users> users = usersRepository.findById(jwtTokenProvider.extractSubjectFromJwt(accessToken));
        if (users.isPresent()) {
            return users.get();
        } else {
            throw new RuntimeException("토큰에 해당하는 멤버가 없습니다.");
        }
    }

    @Transactional
    @Async
    public String schoolEmail(String accessToken, SchoolDto.RequestForUnivCode schoolDto) {
        usersRepository.findById(jwtTokenProvider.extractSubjectFromJwt(accessToken)).orElseThrow(()
                -> new RuntimeException("해당하는 유저가 없습니다."));
        MimeMessage message = javaMailSender.createMimeMessage();
        String email = schoolDto.getEmail();
        try {
            // 메시지 전송
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setTo(email);
            messageHelper.setSubject("모여타 인증 번호");
            messageHelper.setFrom("moyeota6340@gmail.com", "모여타 팀 메일");
            String verificationCode = generateVerificationCode();
            StringBuffer sb = new StringBuffer();
            sb.append("<html><body>");
            sb.append("<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>");
            sb.append("<h1>" + "[모여타 어플리케이션]" + "</h1><br>");
            sb.append("아래 코드 이용하여 인증하세요<br><br>");
            sb.append("<h3>인증코드 : ").append(verificationCode);
            sb.append("</h3>");
            sb.append("</body></html>");
            String str = sb.toString();
            messageHelper.setText(str, true);
            // 메시지 인증 코드 DB 저장
//            if (schoolEmailRepository.findByEmail(email).isPresent()) {
//                schoolEmailRepository.delete(schoolEmailRepository.findByEmail(email).get());
//            }
            // 메시지 인증 코드 Redis 저장
            if (redisRepository.findByEmail(email).isPresent()) {
                redisRepository.delete(redisRepository.findByEmail(email).get());
            }
            // SchoolEmail schoolEmail = SchoolEmail.builder().email(email).code(verificationCode).build();
            SchoolEmailRedis schoolEmailRedis = SchoolEmailRedis.builder()
                    .email(email)
                    .code(verificationCode)
                    .build();
            // schoolEmailRepository.save(schoolEmail);
            redisRepository.save(schoolEmailRedis);
            javaMailSender.send(message);
            // redis 전송
            // redisUtil.setDataExpire(verificationCode, email, 60 * 5L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return schoolDto.getEmail();
    }

    @Transactional
    public SchoolDto.ResponseSuccess schoolEmailCheck(String accessToken, SchoolDto.RequestForUnivCodeCheck schoolDto) {
        Users users = usersRepository.findById(jwtTokenProvider.extractSubjectFromJwt(accessToken)).orElseThrow(()
                -> new RuntimeException("해당하는 유저가 없습니다."));
        log.info("user 찾음");
//        SchoolEmail bySchoolEmail = schoolEmailRepository.findByEmail(schoolDto.getEmail()).orElseThrow(() -> new RuntimeException("해당하는 이메일이 존재하지 않습니다."));
        log.info("school Email = {}", redisRepository.findByEmail(schoolDto.getEmail()));
        SchoolEmailRedis schoolEmailRedis = redisRepository.findByEmail(schoolDto.getEmail()).orElseThrow(() -> new RuntimeException("해당하는 이메일이 존재하지 않습니다."));

//        if (bySchoolEmail.getCode().equals(schoolDto.getCode())) {
//            users.updateSchoolAuthenticate(schoolDto.getUnivName());
//            return SchoolDto.ResponseSuccess.builder()
//                    .univName(schoolDto.getUnivName())
//                    .certified_email(schoolDto.getEmail())
//                    .build();
//        }
        if (schoolEmailRedis.getCode().equals(schoolDto.getCode())) {
            users.updateSchoolAuthenticate(schoolDto.getUnivName());
            return SchoolDto.ResponseSuccess.builder()
                    .univName(schoolDto.getUnivName())
                    .certified_email(schoolDto.getEmail())
                    .build();
        }
        throw new RuntimeException("코드가 일치하지 않습니다. 코드를 다시 확인하세요");
    }

    public String findNameByUserId(Long userId) {
        return usersRepository.findNameByUserId(userId);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%04d", random.nextInt(10000));
    }

    @Transactional
    public UsersResponseDto createNickName(String tokenInfo, String nickname) {
        Users users = usersRepository.findById(jwtTokenProvider.extractSubjectFromJwt(tokenInfo)).orElseThrow(()
                -> new RuntimeException("해당하는 유저가 없습니다."));
        users.createNickName(nickname);

        return UsersResponseDto.builder()
                .id(users.getId())
                .loginId(users.getLoginId())
                .name(users.getName())
                .nickName(users.getNickName())
                .profileImage(users.getProfileImage())
                .email(users.getEmail())
                .age(users.getAge())
                .status(users.getStatus())
                .averageStarRate(users.getAverageStarRate())
                .school(users.getSchool())
                .gender(users.getGender())
                .build();
    }

    @Transactional
    public UsersResponseDto updateNickName(String tokenInfo, String nickname) {
        Users users = usersRepository.findById(jwtTokenProvider.extractSubjectFromJwt(tokenInfo)).orElseThrow(()
                -> new RuntimeException("해당하는 유저가 없습니다."));
        users.updateNickName(nickname);
        return UsersResponseDto.builder()
                .id(users.getId())
                .loginId(users.getLoginId())
                .name(users.getName())
                .age(users.getAge())
                .nickName(users.getNickName())
                .profileImage(users.getProfileImage())
                .email(users.getEmail())
                .status(users.getStatus())
                .averageStarRate(users.getAverageStarRate())
                .school(users.getSchool())
                .gender(users.getGender())
                .build();
    }
}