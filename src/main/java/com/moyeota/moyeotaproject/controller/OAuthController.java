package com.moyeota.moyeotaproject.controller;

import com.moyeota.moyeotaproject.dto.UsersDto.TokenInfoDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moyeota.moyeotaproject.dto.OAuthDto.OAuthLoginParams.GoogleLoginParams;
import com.moyeota.moyeotaproject.dto.OAuthDto.OAuthLoginParams.KakaoLoginParams;
import com.moyeota.moyeotaproject.dto.OAuthDto.OAuthLoginParams.NaverLoginParams;
import com.moyeota.moyeotaproject.config.response.ResponseDto;
import com.moyeota.moyeotaproject.config.response.ResponseUtil;
import com.moyeota.moyeotaproject.service.OAuthLoginService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api(tags = "OAuth", description = "OAuth Login Controller")
@ApiResponses({
	@ApiResponse(code = 200, message = "API 정상 작동"),
	@ApiResponse(code = 400, message = "BAD REQUEST"),
	@ApiResponse(code = 404, message = "NOT FOUND"),
	@ApiResponse(code = 500, message = "INTERNAL SERVER ERROR")
})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/oauth")
@Slf4j
public class OAuthController {

	private final OAuthLoginService oAuthLoginService;

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "구글 소셜 로그인", notes = "구글 소셜 로그인 회원가입 API")
	@PostMapping("/google")
	public ResponseDto<TokenInfoDto> loginGoogle(@RequestBody GoogleLoginParams params) {
		return ResponseUtil.SUCCESS("구글에 로그인 성공하였습니다. ", oAuthLoginService.login(params));
	}

	@ApiOperation(value = "카카오 소셜 로그인", notes = "카카오 소셜 로그인 회원가입 API")
	@PostMapping("/kakao")
	public ResponseDto<TokenInfoDto> loginKakao(@RequestBody KakaoLoginParams params) {
		return ResponseUtil.SUCCESS("카카오 로그인 성공하였습니다. ", oAuthLoginService.login(params));
	}

	@ApiOperation(value = "네이버 소셜 로그인", notes = "네이버 소셜 로그인 회원가입 API")
	@PostMapping("/naver")
	public ResponseDto<TokenInfoDto> loginNaver(@RequestBody NaverLoginParams params) {
		return ResponseUtil.SUCCESS("네이버 로그인 성공하였습니다. ", oAuthLoginService.login(params));
	}
}
