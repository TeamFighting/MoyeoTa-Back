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
    private final TokenService tokenService;

    @ApiOperation(value = "사용자 정보 받기", notes = "사용자 정보 API")
    @GetMapping("")
    public ResponseDto getUserInfo(@RequestHeader(value = "Authorization") String tokenInfo) {
        return ResponseUtil.SUCCESS("사용자 정보를 받아왔습니다.", usersService.getInfo(tokenInfo));
    }

    @ApiOperation(value = "토큰 재발급", notes = "리프레쉬 토큰을 이용한 토큰 재발급")
    @PostMapping("/refresh-token")
    public ResponseDto generateToken(@RequestBody RefreshTokenRequest request) {
        return ResponseUtil.SUCCESS("토큰을 재발급하였습니다.", tokenService.generateRefreshToken(request));
    }

    @ApiOperation(value = "사용자 정보 수정", notes = "사용자 정보 수정 및 추가 API")
    @PutMapping("/info")
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

    @ApiOperation(value = "닉네임 생성", notes = "닉네임 생성 API")
    @PostMapping("/nickname")
    public ResponseDto createNickName(@RequestHeader(value = "Authorization") String tokenInfo, @RequestBody UsersDto.updateNickName usersDto) {
        return ResponseUtil.SUCCESS("닉네임이 생성되었습니다.", usersService.createNickName(tokenInfo, usersDto.getNickName()));
    }

    @ApiOperation(value = "닉네임 수정", notes = "닉네임 수정 API")
    @PutMapping("/nickname")
    public ResponseDto updateNickname(@RequestHeader(value = "Authorization") String tokenInfo, @RequestBody UsersDto.updateNickName usersDto) {
        return ResponseUtil.SUCCESS("닉네임이 변경되었습니다.", usersService.updateNickName(tokenInfo, usersDto.getNickName()));
    }

    @ApiOperation(value = "사용자 이미지 변경", notes = "사용자 이미지 변경을 위한 API")
    @PostMapping("/profile-Image")
    public ResponseDto updateProfileImage(@RequestHeader(value = "Authorization") String tokenInfo, @RequestParam("file") MultipartFile profileImage) {
        return ResponseUtil.SUCCESS("사용자의 프로필 이미지가 변경되었습니다.", usersService.updateProfileImage(tokenInfo, profileImage));
    }

    @ApiOperation(value = "사용자 삭제", notes = "사용자 탈퇴를 위한 API")
    @DeleteMapping()
    public ResponseDto deleteUser(@RequestHeader(value = "Authorization") String tokenInfo) {
        return ResponseUtil.SUCCESS("사용자 탈퇴 및 데이터를 삭제하였습니다.", usersService.deleteUser(tokenInfo));
    }
}