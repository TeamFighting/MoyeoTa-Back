package com.moyeota.moyeotaproject.dto.UsersDto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class TokenInfoDto {

	private final Long userId;
	private final String accessToken;
	private final String refreshToken;
	private final String grantType;
	private final Long expiresIn;

	public static TokenInfoDto of(Long userId, String accessToken, String refreshToken, String grantType,
		Long expiresIn) {
		return new TokenInfoDto(userId, accessToken, refreshToken, grantType, expiresIn);
	}
}
