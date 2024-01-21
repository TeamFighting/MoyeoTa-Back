package com.moyeota.moyeotaproject.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.moyeota.moyeotaproject.config.jwtConfig.JwtTokenProvider;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ImageService {

    private final UsersRepository usersRepository;
    private final AmazonS3Client amazonS3Client;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Transactional
    public String defaultProfileImage() {
        int randomNumber = generateRandomNumber();
        String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/%08defaultProfileImage" + randomNumber + ".png";
        return fileUrl;
    }

    public int generateRandomNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(4) + 1;
        return randomNumber;
    }

    @Transactional
    public String updateProfileImage(String accessToken, MultipartFile profileImage) {
        Users users = usersRepository.findById(jwtTokenProvider.extractSubjectFromJwt(accessToken)).orElseThrow(()
                -> new RuntimeException("해당하는 유저가 없습니다."));
        try {
            String fileName = users.getId() + "_" + Instant.now().toEpochMilli() + "_" + sanitizeFileName(profileImage.getOriginalFilename());

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
            throw new RuntimeException(e.getMessage());
        }
    }

    // 특수문자나 공백 등을 제거하여 안전한 파일 이름 생성
    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
    }
}
