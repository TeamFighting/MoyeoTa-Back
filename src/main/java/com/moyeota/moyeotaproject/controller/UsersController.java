package com.moyeota.moyeotaproject.controller;

import com.moyeota.moyeotaproject.config.response.ResponseDto;
import com.moyeota.moyeotaproject.config.response.ResponseUtil;
import com.moyeota.moyeotaproject.controller.dto.SchoolRequestDto;
import com.moyeota.moyeotaproject.controller.dto.SignUpRequestDto;
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

    // 자체 회원가입 -> 필요없을 시 삭제
    @PostMapping("/signup")
    public ResponseDto signup(@RequestBody SignUpRequestDto signUpRequestDto) {
        usersService.signup(signUpRequestDto);
        return ResponseUtil.SUCCESS("회원가입에 성공하였습니다.", signUpRequestDto.getEmail());
    }

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
    public ResponseDto addInfo(HttpServletRequest request, @RequestBody SignUpRequestDto signUpRequestDto) {
        return ResponseUtil.SUCCESS("추가 정보 입력을 완료하였습니다", usersService.addInfo(request.getHeader("Authorization"), signUpRequestDto));
    }

    @PostMapping("/school-email")
    public ResponseDto schoolEmail(@RequestBody SchoolRequestDto schoolRequestDto) throws IOException {
        return ResponseUtil.SUCCESS("학교 인증 메일이 전송되었습니다", usersService.schoolEmail(schoolRequestDto));
    }

    @PostMapping("/school-email-check")
    public ResponseDto schoolEmailCheck(@RequestBody SchoolRequestDto schoolRequestDto) throws IOException {
        return ResponseUtil.SUCCESS("학교 인증이 완료되었습니다.", usersService.schoolEmailCheck(schoolRequestDto));
    }
}
