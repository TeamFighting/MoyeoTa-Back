package com.moyeota.moyeotaproject.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Objects;
import java.util.Random;

import javax.imageio.ImageIO;

import org.marvinproject.image.transform.scale.Scale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.moyeota.moyeotaproject.config.exception.ApiException;
import com.moyeota.moyeotaproject.config.exception.ErrorCode;
import com.moyeota.moyeotaproject.config.jwtConfig.JwtTokenProvider;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marvin.image.MarvinImage;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ImageService {
	private static final int targetImageSize = 86;

	private final UsersRepository usersRepository;
	private final AmazonS3Client amazonS3Client;
	private final JwtTokenProvider jwtTokenProvider;
	private final ResourceLoader resourceLoader;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${cloud.aws.region.static}")
	private String region;

	@Transactional
	public String getResizedImageUrl(String imageUrl) {
		Resource imageResource = resourceLoader.getResource(imageUrl);

		if (!imageResource.exists()) {
			throw new ApiException(ErrorCode.FILE_RESIZING_ERROR);
		}

		try (InputStream inputStream = imageResource.getInputStream()) {
			BufferedImage originImage = ImageIO.read(inputStream);

			String fileName = Instant.now().toEpochMilli() + "_" + sanitizeFileName(
				Objects.requireNonNull(imageUrl));
			String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + fileName;
			String fileFormatName = "JPEG";

			MultipartFile resizedFile = resizeImage(fileName, fileFormatName, originImage, 86);

			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(resizedFile.getContentType());
			metadata.setContentLength(resizedFile.getSize());

			amazonS3Client.putObject(bucket, fileName, resizedFile.getInputStream(), metadata);
			return fileUrl;
		} catch(IOException e) {
			throw new ApiException(ErrorCode.FILE_RESIZING_ERROR);
		}
	}

	@Transactional
	public Resource getImage(String imageUrl) {
		return resourceLoader.getResource(imageUrl);
	}

	@Transactional
	public String defaultProfileImageWithToken(String accessToken) {
		Users users = usersRepository.findById(jwtTokenProvider.extractSubjectFromJwt(accessToken)).orElseThrow(()
			-> new RuntimeException("해당하는 유저가 없습니다."));
		String fileUrl = defaultProfileImage();
		users.setDefaultProfileImage(fileUrl);
		return fileUrl;
	}

	@Transactional
	public String defaultProfileImage() {
		int randomNumber = generateRandomNumber();
		return "https://" + bucket + ".s3." + region + ".amazonaws.com/%08defaultProfileImage" + randomNumber + ".png";
	}

	public int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(4) + 1;
	}

	@Transactional
	public String updateProfileImage(String accessToken, MultipartFile profileImage) {
		Users users = usersRepository.findById(jwtTokenProvider.extractSubjectFromJwt(accessToken)).orElseThrow(()
			-> new ApiException(ErrorCode.INVALID_USER));
		try {
			BufferedImage originImage = ImageIO.read(profileImage.getInputStream());
			String fileName = users.getId() + "_" + Instant.now().toEpochMilli() + "_" + sanitizeFileName(
				Objects.requireNonNull(profileImage.getOriginalFilename()));
			String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + fileName;
			String fileFormatName = profileImage.getContentType()
				.substring(profileImage.getContentType().lastIndexOf("/") + 1);

			MultipartFile resizedFile = resizeImage(fileName, fileFormatName, originImage, 86);

			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(profileImage.getContentType());
			metadata.setContentLength(resizedFile.getSize());

			amazonS3Client.putObject(bucket, fileName, resizedFile.getInputStream(), metadata);
			log.info("users ={}", users.getProfileImage());
			users.updateProfileImage(fileUrl);
			log.info("users ={}", users.getProfileImage());
			return users.getName() + "의 프로필이미지가 " + fileUrl + " 로 변경되었습니다.";
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	MultipartFile resizeImage(String fileName, String fileFormatName, BufferedImage originalImage, int targetSize) {
		try {
			MarvinImage marvinImage = new MarvinImage(originalImage);

			Scale scale = new Scale();
			scale.load();
			scale.setAttribute("newWidth", targetSize);
			scale.setAttribute("newHeight", targetSize);
			scale.process(marvinImage.clone(), marvinImage, null, null, false);

			BufferedImage bufferedImage = marvinImage.getBufferedImageNoAlpha();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, fileFormatName, baos);
			baos.flush();

			return new MockMultipartFile(fileName, baos.toByteArray());
		} catch (IOException e) {
			throw new ApiException(ErrorCode.FILE_RESIZING_ERROR);
		}
	}

	// 특수문자나 공백 등을 제거하여 안전한 파일 이름 생성
	private String sanitizeFileName(String fileName) {
		return fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
	}
}
