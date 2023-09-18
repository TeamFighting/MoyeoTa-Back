package com.moyeota.moyeotaproject.controller;

import com.moyeota.moyeotaproject.config.response.ResponseDto;
import com.moyeota.moyeotaproject.config.response.ResponseUtil;
import com.moyeota.moyeotaproject.controller.dto.KakaoApproveResponse;
import com.moyeota.moyeotaproject.service.KakaoPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// 카카오페이 테스트용
@RestController
@RequiredArgsConstructor
public class PayController {
    private final KakaoPayService kakaoPayService;

    @PostMapping("/kakaoPay")
    public String kakaoPay() {
        return "redirect:" + kakaoPayService.kakaoPayReady();
    }

    @GetMapping("/kakaoPaySuccess")
    public void kakaoPaySuccess(@RequestParam("pg_token") String pg_token, Model model) {
    }

}
