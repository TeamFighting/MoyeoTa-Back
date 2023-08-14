package com.moyeota.moyeotaproject.controller;

import com.moyeota.moyeotaproject.config.ResponseDto;
import com.moyeota.moyeotaproject.config.ResponseUtil;
import com.moyeota.moyeotaproject.domain.participationDetails.ParticipationDetails;
import com.moyeota.moyeotaproject.service.ParticipationDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/participationDetails")
public class ParticipationDetailsController {

    private final ParticipationDetailsService participationDetailsService;

    //참가 신청 API
    @PostMapping("/{userId}/{postId}")
    public ResponseDto join(@PathVariable("userId") Long userId, @PathVariable("postId") Long postId) {
        Long participationDetailsId = participationDetailsService.join(userId, postId);
        return ResponseUtil.SUCCESS("참가 신청이 완료되었습니다.", participationDetailsId);
    }

    //참가 취소 API
    @PostMapping("/{participationDetailsId}")
    public ResponseDto cancel(@PathVariable("participationDetailsId") Long participationDetailsId) {
        ParticipationDetails participationDetails = participationDetailsService.findById(participationDetailsId);
        participationDetails.cancel();
        return ResponseUtil.SUCCESS("참가 취소가 완료되었습니다.", participationDetailsId);
    }

}
