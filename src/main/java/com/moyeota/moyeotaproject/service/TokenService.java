package com.moyeota.moyeotaproject.service;

import org.springframework.stereotype.Service;

import com.moyeota.moyeotaproject.config.exception.ApiException;
import com.moyeota.moyeotaproject.config.exception.ErrorCode;
import com.moyeota.moyeotaproject.config.jwtconfig.JwtTokenGenerator;
import com.moyeota.moyeotaproject.config.jwtconfig.JwtTokenProvider;
import com.moyeota.moyeotaproject.controller.dto.RefreshTokenRequest;
import com.moyeota.moyeotaproject.controller.dto.TokenInfoDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

	private final JwtTokenProvider jwtTokenProvider;
	private final JwtTokenGenerator jwtTokenGenerator;

	public TokenInfoDto generateRefreshToken(RefreshTokenRequest request) {
		String refreshToken = request.getRefreshToken();
		if (jwtTokenProvider.validateToken(refreshToken)) {
			Long usersId = jwtTokenProvider.extractSubjectFromJwt(refreshToken);
			return jwtTokenGenerator.generate(usersId);
		} else {
			throw new ApiException(ErrorCode.EXPIRED_TOKEN);
		}
	}
}
