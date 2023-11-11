package com.moyeota.moyeotaproject.controller;

import com.moyeota.moyeotaproject.config.response.ResponseDto;
import com.moyeota.moyeotaproject.config.response.ResponseUtil;
import com.moyeota.moyeotaproject.controller.dto.RefreshTokenRequest;
import com.moyeota.moyeotaproject.controller.dto.SchoolDto;
import com.moyeota.moyeotaproject.controller.dto.UsersDto;
import com.moyeota.moyeotaproject.component.OAuth.OAuthLoginParams.GoogleLoginParams;
import com.moyeota.moyeotaproject.component.OAuth.OAuthLoginParams.KakaoLoginParams;
import com.moyeota.moyeotaproject.component.OAuth.OAuthLoginParams.NaverLoginParams;
import com.moyeota.moyeotaproject.service.OAuthLoginService;
import com.moyeota.moyeotaproject.service.TokenService;
import com.moyeota.moyeotaproject.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Api(tags = "Users", description = "Users Controller")
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
    private final OAuthLoginService oAuthLoginService;
    private final TokenService tokenService;

    @ApiOperation(value = "토큰 재발급", notes = "리프레쉬 토큰을 이용한 토큰 재발급")
    @PostMapping("/refresh-token")
    public ResponseDto generateToken(@RequestBody RefreshTokenRequest request) {
        return ResponseUtil.SUCCESS("토큰을 재발급하였습니다.", tokenService.generateRefreshToken(request));
    }

    @ApiOperation(value = "구글 소셜 로그인", notes = "구글 소셜 로그인 회원가입 API")
    @PostMapping("/google")
    public ResponseDto loginGoogle(@RequestBody GoogleLoginParams params) {
        return ResponseUtil.SUCCESS("구글에 로그인 성공하였습니다. ", oAuthLoginService.login(params));
    }

    @ApiOperation(value = "카카오 소셜 로그인", notes = "카카오 소셜 로그인 회원가입 API")
    @PostMapping("/kakao")
    public ResponseDto loginKakao(@RequestBody KakaoLoginParams params) {
        return ResponseUtil.SUCCESS("카카오 로그인 성공하였습니다. ", oAuthLoginService.login(params));
    }

    @ApiOperation(value = "네이버 소셜 로그인", notes = "네이버 소셜 로그인 회원가입 API")
    @PostMapping("/naver")
    public ResponseDto loginNaver(@RequestBody NaverLoginParams params) {
        return ResponseUtil.SUCCESS("네이버 로그인 성공하였습니다. ", oAuthLoginService.login(params));
    }

    @ApiOperation(value = "사용자 정보 수정", notes = "사용자 정보 수정 및 추가 API")
    @PostMapping("/info")
    public ResponseDto updateInfo(@RequestHeader(value = "Authorization") String tokenInfo, @RequestBody UsersDto.updateDto usersDto) {
        return ResponseUtil.SUCCESS("프로필 업데이트를 완료하였습니다", usersService.addInfo(tokenInfo, usersDto));
    }

    @ApiOperation(value = "학교 인증", notes = "학교 인증을 위한 이메일 코드 전송 API")
    @PostMapping("/school-email")
    public ResponseDto schoolEmail(@RequestHeader(value = "Authorization") String tokenInfo, @RequestBody SchoolDto.RequestForUnivCode schoolRequestDto) throws IOException {
        return ResponseUtil.SUCCESS("학교 인증 메일이 전송되었습니다", usersService.schoolEmail(tokenInfo, schoolRequestDto));
    }

    @ApiOperation(value = "학교 인증 코드 확인", notes = "인증 코드 확인 API")
    @PostMapping("/school-email/verification")
    public ResponseDto schoolEmailCheck(@RequestHeader(value = "Authorization") String tokenInfo, @RequestBody SchoolDto.RequestForUnivCodeCheck schoolRequestDto) throws IOException {
        return ResponseUtil.SUCCESS("학교 인증이 완료되었습니다.", usersService.schoolEmailCheck(tokenInfo, schoolRequestDto));
    }

    @ApiOperation(value = "사용자 이미지 변경", notes = "사용자 이미지 변경을 위한 API")
    @PostMapping("/profile-Image")
    public ResponseDto profileImage(@RequestHeader(value = "Authorization") String tokenInfo, @RequestParam("file") MultipartFile profileImage) {
        return ResponseUtil.SUCCESS("사용자의 프로필 이미지가 변경되었습니다.", usersService.changeProfileImage(tokenInfo, profileImage));
    }
}