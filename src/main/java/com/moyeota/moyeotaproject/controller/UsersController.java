package com.moyeota.moyeotaproject.controller;

import com.moyeota.moyeotaproject.config.response.ResponseDto;
import com.moyeota.moyeotaproject.config.response.ResponseUtil;
import com.moyeota.moyeotaproject.controller.dto.SchoolDto;
import com.moyeota.moyeotaproject.controller.dto.UsersDto;
import com.moyeota.moyeotaproject.component.OAuth.OAuthLoginParams.GoogleLoginParams;
import com.moyeota.moyeotaproject.component.OAuth.OAuthLoginParams.KakaoLoginParams;
import com.moyeota.moyeotaproject.component.OAuth.OAuthLoginParams.NaverLoginParams;
import com.moyeota.moyeotaproject.service.OAuthLoginService;
import com.moyeota.moyeotaproject.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @ApiOperation(value = "구글 소셜 로그인", notes = "구글 소셜 로그인 회원가입 API")
    @PostMapping("/google")
    public ResponseDto loginGoogle(@RequestBody GoogleLoginParams params) {
        return ResponseUtil.SUCCESS("구글에 로그인 성공하였습니다. ", oAuthLoginService.login(params));
    }

    @ApiOperation(value = "카카오 소셜 로그인", notes = "카카오 소셜 로그인 회원가입 API")
    @PostMapping("/kakao")
    public ResponseDto loginKakao(@RequestBody KakaoLoginParams params) {
        return ResponseUtil.SUCCESS( "카카오 로그인 성공하였습니다. ", oAuthLoginService.login(params));
    }

    @ApiOperation(value = "네이버 소셜 로그인", notes = "네이버 소셜 로그인 회원가입 API")
    @PostMapping("/naver")
    public ResponseDto loginNaver(@RequestBody NaverLoginParams params) {
        return ResponseUtil.SUCCESS("네이버 로그인 성공하였습니다. ", oAuthLoginService.login(params));
    }

    @ApiOperation(value = "사용자 정보 수정", notes = "사용자 정보 수정 및 추가 API")
    @PostMapping("/user-info-update")
    public ResponseDto updateInfo(HttpServletRequest request, @RequestBody UsersDto.updateDto usersDto) {
        return ResponseUtil.SUCCESS("프로필 업데이트를 완료하였습니다", usersService.addInfo(request.getHeader("Authorization"), usersDto));
    }

    @ApiOperation(value = "학교 인증", notes = "학교 인증을 위한 이메일 코드 전송 API")
    @PostMapping("/school-email")
    public ResponseDto schoolEmail(@RequestBody SchoolDto.Request schoolRequestDto) throws IOException {
        return ResponseUtil.SUCCESS("학교 인증 메일이 전송되었습니다", usersService.schoolEmail(schoolRequestDto));
    }

    @ApiOperation(value = "학교 인증 코드 확인", notes = "인증 코드 확인 API")
    @PostMapping("/school-email-check")
    public ResponseDto schoolEmailCheck(@RequestBody SchoolDto.Request schoolRequestDto) throws IOException {
        return ResponseUtil.SUCCESS("학교 인증이 완료되었습니다.", usersService.schoolEmailCheck(schoolRequestDto));
    }
}
