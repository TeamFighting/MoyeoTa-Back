package com.moyeota.moyeotaproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moyeota.moyeotaproject.config.ResponseDto;
import com.moyeota.moyeotaproject.config.ResponseUtil;
import com.moyeota.moyeotaproject.controller.dto.SignUpRequestDto;
import com.moyeota.moyeotaproject.domain.users.OAuth.OAuthLoginParams.KakaoLoginParams;
import com.moyeota.moyeotaproject.domain.users.OAuth.OAuthLoginParams.NaverLoginParams;
import com.moyeota.moyeotaproject.service.OAuthLoginService;
import com.moyeota.moyeotaproject.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UsersService usersService;
    private final OAuthLoginService oAuthLoginService;

    @PostMapping("/signup")
    public ResponseDto signup(@RequestBody SignUpRequestDto signUpRequestDto) {
        usersService.signup(signUpRequestDto);
        return ResponseUtil.SUCCESS("회원가입에 성공하였습니다.", signUpRequestDto.getEmail());
    }

    @PostMapping("/google")
    public ResponseDto oauth2Signup(@RequestParam String authorizationCode) throws ParseException, JsonProcessingException {
        return ResponseUtil.SUCCESS("구글에 회원가입 성공하였습니다. ", usersService.oauth2Signup(authorizationCode));
    }

    @PostMapping("/kakao")
    public ResponseDto loginKakao(@RequestBody KakaoLoginParams params) {
        return ResponseUtil.SUCCESS( "카카오 로그인 성공하였습니다. ", oAuthLoginService.login(params));
    }

    @PostMapping("/naver")
    public ResponseDto loginNaver(@RequestBody NaverLoginParams params) {
        return ResponseUtil.SUCCESS("네이버 로그인 성공하였습니다. ", oAuthLoginService.login(params));
    }
}
