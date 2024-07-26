package com.moyeota.moyeotaproject.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moyeota.moyeotaproject.dto.OAuthDto.OAuthInfoResponse.OAuthInfoResponse;
import com.moyeota.moyeotaproject.dto.OAuthDto.OAuthLoginParams.OAuthLoginParams;
import com.moyeota.moyeotaproject.config.exception.ApiException;
import com.moyeota.moyeotaproject.config.exception.ErrorCode;
import com.moyeota.moyeotaproject.config.jwtConfig.JwtTokenGenerator;
import com.moyeota.moyeotaproject.dto.UsersDto.TokenInfoDto;
import com.moyeota.moyeotaproject.domain.oAuth.OAuth;
import com.moyeota.moyeotaproject.domain.oAuth.OAuthRepository;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
	private final ImageService imageService;

	private static final String KAKAO_DEFAULT_IMAGE = "https://ssl.pstatic.net/static/pwe/address/img_profile.png";

	public TokenInfoDto login(OAuthLoginParams params) {
		// authorizeToken을 이용해서 소셜 리소스 서버에서 정보를 가져옴.
		OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
		// 새로 생성 or 기존 사용자 userId 가져오기
		Long userId = findOrCreateUserAndOAuth(oAuthInfoResponse);
		Users user = getUserById(userId);
		return jwtTokenGenerator.generate(user.getId());
	}

	private Long findOrCreateUserAndOAuth(OAuthInfoResponse oAuthInfoResponse) {
		// 소셜 로그인 이름 가져오기
		String oAuthProvider = oAuthInfoResponse.getOAuthProvider().name();
		String userEmail = oAuthInfoResponse.getEmail();
		if (userEmail.isEmpty()) {
			throw new ApiException(ErrorCode.NO_EMAIL_ERROR);
		}

		// OAuth 데이터베이스에서 소셜 provider와 Email로 해당 사용자 정보 찾음
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
			.profileImage(getProfileImage(oAuthInfoResponse))
			.age(oAuthInfoResponse.getAge())
			.loginId(oAuthInfoResponse.getOAuthProvider().name() + " " + oAuthInfoResponse.getEmail())
			.password(passwordEncoder.encode(oAuthInfoResponse.getOAuthProvider().name()))
			.phoneNumber(oAuthInfoResponse.getPhoneNumber())
			.build();
	}

	private String getProfileImage(OAuthInfoResponse oAuthInfoResponse) {
		log.error("userProfileImageURL={}", oAuthInfoResponse.getProfileImage());
		// 프로필 이미지를 못 가져올 경우 기본이미지 설정
		if (oAuthInfoResponse.getProfileImage() == null) {
			return imageService.defaultProfileImage();
		}
		// 카카오 기본 이미지일 경우 모여타 기본이미지로 변경해줌
		if (oAuthInfoResponse.getProfileImage().equals(KAKAO_DEFAULT_IMAGE)) {
			return imageService.defaultProfileImage();
		}
		return oAuthInfoResponse.getProfileImage();
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
			.orElseThrow(() -> new ApiException(ErrorCode.INVALID_USER));
	}

}
