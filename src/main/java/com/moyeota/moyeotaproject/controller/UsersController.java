package com.moyeota.moyeotaproject.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.moyeota.moyeotaproject.config.response.ResponseDto;
import com.moyeota.moyeotaproject.config.response.ResponseUtil;
import com.moyeota.moyeotaproject.controller.dto.RefreshTokenRequest;
import com.moyeota.moyeotaproject.controller.dto.SchoolDto;
import com.moyeota.moyeotaproject.controller.dto.TokenInfoDto;
import com.moyeota.moyeotaproject.controller.dto.UserDto;
import com.moyeota.moyeotaproject.controller.dto.UsersDto.UsersResponseDto;
import com.moyeota.moyeotaproject.service.ImageService;
import com.moyeota.moyeotaproject.service.TokenService;
import com.moyeota.moyeotaproject.service.UsersService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@Api(tags = "Users")
@ApiResponses({
	@ApiResponse(code = 200, message = "API 정상 작동"),
	@ApiResponse(code = 400, message = "BAD REQUEST"),
	@ApiResponse(code = 404, message = "NOT FOUND"),
	@ApiResponse(code = 500, message = "INTERNAL SERVER ERROR")
})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UsersController {

	private final UsersService usersService;
	private final TokenService tokenService;
	private final ImageService imageService;

	@ApiOperation(value = "사용자 정보 받기", notes = "사용자 정보 API")
	@GetMapping("")
	public ResponseDto<UserDto.Response> getUserInfo(@RequestHeader(value = "Authorization") String tokenInfo) {
		return ResponseUtil.SUCCESS("사용자 정보를 받아왔습니다.", usersService.getInfo(tokenInfo));
	}

	@ApiOperation(value = "토큰 재발급", notes = "리프레쉬 토큰을 이용한 토큰 재발급")
	@PostMapping("/refresh-token")
	public ResponseDto<TokenInfoDto> generateToken(@RequestBody RefreshTokenRequest request) {
		return ResponseUtil.SUCCESS("토큰을 재발급하였습니다.", tokenService.generateRefreshToken(request));
	}

	@ApiOperation(value = "사용자 정보 수정", notes = "사용자 정보 수정 및 추가 API")
	@PutMapping("/info")
	public ResponseDto<UsersResponseDto> updateInfo(@RequestHeader(value = "Authorization") String tokenInfo,
		@RequestBody UserDto.updateDto usersDto) {
		return ResponseUtil.SUCCESS("프로필 업데이트를 완료하였습니다", usersService.addInfo(tokenInfo, usersDto));
	}

	@ApiOperation(value = "학교 인증", notes = "학교 인증을 위한 이메일 코드 전송 API")
	@PostMapping("/school-email")
	public ResponseDto<String> schoolEmail(@RequestHeader(value = "Authorization") String tokenInfo,
		@RequestBody SchoolDto.RequestForUnivCode schoolRequestDto) throws IOException {
		return ResponseUtil.SUCCESS("학교 인증 메일이 전송되었습니다", usersService.schoolEmail(tokenInfo, schoolRequestDto));
	}

	@ApiOperation(value = "학교 인증 코드 확인", notes = "인증 코드 확인 API")
	@PostMapping("/school-email/verification")
	public ResponseDto<SchoolDto.ResponseSuccess> schoolEmailCheck(
		@RequestHeader(value = "Authorization") String tokenInfo,
		@RequestBody SchoolDto.RequestForUnivCodeCheck schoolRequestDto) {
		return ResponseUtil.SUCCESS("학교 인증이 완료되었습니다.", usersService.schoolEmailCheck(tokenInfo, schoolRequestDto));
	}

	@ApiOperation(value = "닉네임 생성", notes = "닉네임 생성 API")
	@PostMapping("/nickname")
	public ResponseDto<UsersResponseDto> createNickName(@RequestHeader(value = "Authorization") String tokenInfo,
		@RequestBody UserDto.updateNickName usersDto) {
		return ResponseUtil.SUCCESS("닉네임이 생성되었습니다.", usersService.createNickName(tokenInfo, usersDto.getNickName()));
	}

	@ApiOperation(value = "닉네임 수정", notes = "닉네임 수정 API")
	@PutMapping("/nickname")
	public ResponseDto<UsersResponseDto> updateNickname(@RequestHeader(value = "Authorization") String tokenInfo,
		@RequestBody UserDto.updateNickName usersDto) {
		return ResponseUtil.SUCCESS("닉네임이 변경되었습니다.", usersService.updateNickName(tokenInfo, usersDto.getNickName()));
	}

	@ApiOperation(value = "사용자 이미지 DEAFULT 이미지 설정", notes = "사용자 이미지 DEFAULT 이미지 설정 테스트 API(테스트용)")
	@PostMapping("/default/profile-Image")
	public ResponseDto<String> setProfileImageDefault(@RequestHeader(value = "Authorization") String tokenInfo) {
		return ResponseUtil.SUCCESS("사용자의 프로필 이미지가 Default이미지로 변경되었습니다.",
			imageService.defaultProfileImageWithToken(tokenInfo));
	}

	@ApiOperation(value = "사용자 이미지 변경", notes = "사용자 이미지 변경을 위한 API")
	@PostMapping("/profile-Image")
	public ResponseDto<String> updateProfileImage(@RequestHeader(value = "Authorization") String tokenInfo,
		@RequestParam("file") MultipartFile profileImage) {
		return ResponseUtil.SUCCESS("사용자의 프로필 이미지가 변경되었습니다.", imageService.updateProfileImage(tokenInfo, profileImage));
	}

	@ApiOperation(value = "사용자 삭제", notes = "사용자 탈퇴를 위한 API")
	@DeleteMapping()
	public ResponseDto<UserDto.deleteDto> deleteUser(@RequestHeader(value = "Authorization") String tokenInfo) {
		return ResponseUtil.SUCCESS("사용자 탈퇴 및 데이터를 삭제하였습니다.", usersService.deleteUser(tokenInfo));
	}
}