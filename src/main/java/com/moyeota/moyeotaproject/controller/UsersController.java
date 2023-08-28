package com.moyeota.moyeotaproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moyeota.moyeotaproject.config.ResponseDto;
import com.moyeota.moyeotaproject.config.ResponseUtil;
import com.moyeota.moyeotaproject.controller.dto.SignUpRequestDto;
import com.moyeota.moyeotaproject.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/signup")
    public ResponseDto signup(@RequestBody SignUpRequestDto signUpRequestDto) {
        usersService.signup(signUpRequestDto);
        return ResponseUtil.SUCCESS("회원가입에 성공하였습니다.", signUpRequestDto.getEmail());
    }

    @PostMapping("/oauth2/signup")
    public ResponseDto oauth2Signup(@RequestParam String oauth2AccessToken, @RequestParam String socialLogin) throws ParseException, JsonProcessingException {
        usersService.oauth2Signup(oauth2AccessToken, socialLogin);
        return ResponseUtil.SUCCESS(socialLogin + "에 회원가입 성공하였습니다. ", socialLogin);
    }

    @PostMapping("/oauth2/login")
    public ResponseDto oauth2Login(@RequestParam String oauth2AccessToken, @RequestParam String socialLogin) {
        // usersService.oauth2Login(oauth2AccessToken, socialLogin);
        return ResponseUtil.SUCCESS(socialLogin + "에 로그인 성공하였습니다. ", socialLogin);
    }
}
