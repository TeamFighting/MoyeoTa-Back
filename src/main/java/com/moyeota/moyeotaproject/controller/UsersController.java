package com.moyeota.moyeotaproject.controller;

import com.moyeota.moyeotaproject.config.ResponseDto;
import com.moyeota.moyeotaproject.config.ResponseUtil;
import com.moyeota.moyeotaproject.controller.dto.SchoolDto;
import com.moyeota.moyeotaproject.controller.dto.SignUpDto;
import com.moyeota.moyeotaproject.domain.users.OAuth.OAuthLoginParams.GoogleLoginParams;
import com.moyeota.moyeotaproject.domain.users.OAuth.OAuthLoginParams.KakaoLoginParams;
import com.moyeota.moyeotaproject.domain.users.OAuth.OAuthLoginParams.NaverLoginParams;
import com.moyeota.moyeotaproject.service.OAuthLoginService;
import com.moyeota.moyeotaproject.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UsersService usersService;
    private final OAuthLoginService oAuthLoginService;

    @PostMapping("/google")
    public ResponseDto loginGoogle(@RequestBody GoogleLoginParams params) {
        return ResponseUtil.SUCCESS("구글에 로그인 성공하였습니다. ", oAuthLoginService.login(params));
    }

    @PostMapping("/kakao")
    public ResponseDto loginKakao(@RequestBody KakaoLoginParams params) {
        return ResponseUtil.SUCCESS( "카카오 로그인 성공하였습니다. ", oAuthLoginService.login(params));
    }

    @PostMapping("/naver")
    public ResponseDto loginNaver(@RequestBody NaverLoginParams params) {
        return ResponseUtil.SUCCESS("네이버 로그인 성공하였습니다. ", oAuthLoginService.login(params));
    }

    @PostMapping("/user-additional-info")
    public ResponseDto addInfo(HttpServletRequest request, @RequestBody SignUpDto.Request signUpRequestDto) {
        return ResponseUtil.SUCCESS("추가 정보 입력을 완료하였습니다", usersService.addInfo(request.getHeader("Authorization"), signUpRequestDto));
    }

    @PostMapping("/school-email")
    public ResponseDto schoolEmail(@RequestBody SchoolDto.Request schoolDto) throws IOException {
        return ResponseUtil.SUCCESS("학교 인증 메일이 전송되었습니다", usersService.schoolEmail(schoolDto));
    }

    @PostMapping("/school-email-check")
    public ResponseDto schoolEmailCheck(@RequestBody SchoolDto.Request schoolDto) throws IOException {
        return ResponseUtil.SUCCESS("학교 인증이 완료되었습니다.", usersService.schoolEmailCheck(schoolDto));
    }
}
