package com.moyeota.moyeotaproject.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.moyeota.moyeotaproject.config.jwtConfig.JwtTokenProvider;
import com.moyeota.moyeotaproject.controller.dto.SchoolDto;
import com.moyeota.moyeotaproject.controller.dto.UsersDto;
import com.moyeota.moyeotaproject.domain.schoolEmail.SchoolEmail;
import com.moyeota.moyeotaproject.domain.schoolEmail.SchoolEmailRepository;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AmazonS3Client amazonS3Client;
    private final JavaMailSender javaMailSender;
    private final SchoolEmailRepository schoolEmailRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;


    public UsersDto.Response getInfo(String accessToken) {
        Users users = getUserByToken(accessToken);
        UsersDto.Response usersDto = UsersDto.Response.builder()
                .id(users.getId())
                .loginId(users.getLoginId())
                .name(users.getName())
                .nickName(users.getNickName())
                .profileImage(users.getProfileImage())
                .email(users.getEmail())
                .status(users.getStatus())
                .averageStarRate(users.getAverageStarRate())
                .school(users.getSchool())
                .gender(users.getGender())
                .build();
        return usersDto;
    }

    public UsersDto.Response addInfo(String accessToken, UsersDto.updateDto usersDto) {
        Users users = getUserByToken(accessToken);
        users.updateUsers(usersDto);
        UsersDto.Response updateDto = UsersDto.Response.builder()
                .loginId(users.getLoginId())
                .name(users.getName())
                .nickName(users.getNickName())
                .profileImage(users.getProfileImage())
                .email(users.getEmail())
                .status(users.getStatus())
                .averageStarRate(users.getAverageStarRate())
                .school(users.getSchool())
                .gender(users.getGender())
                .build();
        return updateDto;
    }

    public UsersDto.deleteDto deleteUser(String accessToken) {
        Users users = getUserByToken(accessToken);
        UsersDto.deleteDto deleteDto = UsersDto.deleteDto.builder()
                .name(users.getName())
                .email(users.getEmail()).build();
        usersRepository.delete(users);
        return deleteDto;
    }

    private Users getUserByToken(String accessToken) {
        Optional<Users> users = usersRepository.findById(jwtTokenProvider.extractSubjectFromJwt(accessToken));
        if (users.isPresent()) {
            return users.get();
        } else {
            throw new RuntimeException("토큰에 해당하는 멤버가 없습니다.");
        }
    }

    public String schoolEmail(String accessToken, SchoolDto.RequestForUnivCode schoolDto) {
        usersRepository.findById(jwtTokenProvider.extractSubjectFromJwt(accessToken)).orElseThrow(()
                -> new RuntimeException("해당하는 유저가 없습니다."));
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        String email = schoolDto.getEmail();
        try {
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("학교 인증 코드 번호입니다.");
            String verificationCode = generateVerificationCode();
            simpleMailMessage.setText(String.format("code : %s", verificationCode));
            if (schoolEmailRepository.findByEmail(email).isPresent()) {
                schoolEmailRepository.delete(schoolEmailRepository.findByEmail(email).get());
            }
            SchoolEmail schoolEmail = SchoolEmail.builder().email(email).code(verificationCode).build();
            schoolEmailRepository.save(schoolEmail);
            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return schoolDto.getEmail();
    }

    @Transactional
    public SchoolDto.ResponseSuccess schoolEmailCheck(String accessToken, SchoolDto.RequestForUnivCodeCheck schoolDto) throws IOException {
        Users users = usersRepository.findById(jwtTokenProvider.extractSubjectFromJwt(accessToken)).orElseThrow(()
                -> new RuntimeException("해당하는 유저가 없습니다."));
        SchoolEmail bySchoolEmail = schoolEmailRepository.findByEmail(schoolDto.getEmail()).orElseThrow(() -> new RuntimeException("해당하는 이메일이 존재하지 않습니다."));
        if (bySchoolEmail.getCode().equals(schoolDto.getCode())) {
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

    @Transactional
    public String updateProfileImage(String accessToken, MultipartFile profileImage) {
        Users users = usersRepository.findById(jwtTokenProvider.extractSubjectFromJwt(accessToken)).orElseThrow(()
                -> new RuntimeException("해당하는 유저가 없습니다."));
        try {
            String fileName = profileImage.getOriginalFilename();
            String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + fileName;
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(profileImage.getContentType());
            metadata.setContentLength(profileImage.getSize());
            amazonS3Client.putObject(bucket, fileName, profileImage.getInputStream(), metadata);
            log.info("users ={}", users.getProfileImage());
            users.updateProfileImage(fileUrl);
            log.info("users ={}", users.getProfileImage());
            return users.getName() + "의 프로필이미지가 " + fileUrl + " 로 변경되었습니다.";
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%04d", random.nextInt(10000));
    }

    @Transactional
    public UsersDto.Response createNickName(String tokenInfo, String nickname) {
        Users users = usersRepository.findById(jwtTokenProvider.extractSubjectFromJwt(tokenInfo)).orElseThrow(()
                -> new RuntimeException("해당하는 유저가 없습니다."));
        users.createNickName(nickname);
        UsersDto.Response updateDto = UsersDto.Response.builder()
                .loginId(users.getLoginId())
                .name(users.getName())
                .nickName(users.getNickName())
                .profileImage(users.getProfileImage())
                .email(users.getEmail())
                .status(users.getStatus())
                .averageStarRate(users.getAverageStarRate())
                .school(users.getSchool())
                .gender(users.getGender())
                .build();
        return updateDto;
    }

    @Transactional
    public UsersDto.Response updateNickName(String tokenInfo, String nickname) {
        Users users = usersRepository.findById(jwtTokenProvider.extractSubjectFromJwt(tokenInfo)).orElseThrow(()
                -> new RuntimeException("해당하는 유저가 없습니다."));
        users.updateNickName(nickname);
        UsersDto.Response updateDto = UsersDto.Response.builder()
                .loginId(users.getLoginId())
                .name(users.getName())
                .nickName(users.getNickName())
                .profileImage(users.getProfileImage())
                .email(users.getEmail())
                .status(users.getStatus())
                .averageStarRate(users.getAverageStarRate())
                .school(users.getSchool())
                .gender(users.getGender())
                .build();
        return updateDto;
    }
}