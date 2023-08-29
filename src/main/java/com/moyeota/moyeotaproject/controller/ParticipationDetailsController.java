package com.moyeota.moyeotaproject.controller;

import com.moyeota.moyeotaproject.config.ResponseDto;
import com.moyeota.moyeotaproject.config.ResponseUtil;
import com.moyeota.moyeotaproject.controller.dto.PostsResponseDto;
import com.moyeota.moyeotaproject.domain.participationDetails.ParticipationDetails;
import com.moyeota.moyeotaproject.domain.participationDetails.ParticipationDetailsStatus;
import com.moyeota.moyeotaproject.domain.posts.PostsStatus;
import com.moyeota.moyeotaproject.service.ParticipationDetailsService;
import com.moyeota.moyeotaproject.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/participation-details")
public class ParticipationDetailsController {

    private final ParticipationDetailsService participationDetailsService;
    private final PostsService postsService;

    //참가 신청 API
    @PostMapping("/users/{userId}/posts/{postId}")
    public ResponseDto join(@PathVariable("userId") Long userId, @PathVariable("postId") Long postId) {
        PostsResponseDto responseDto = postsService.findById(postId);
        if(responseDto.getNumberOfParticipants() == responseDto.getNumberOfRecruitment())
            return ResponseUtil.FAILURE("참가 모집이 완료된 공고입니다.", null);
        if(responseDto.getStatus() == PostsStatus.COMPLETE)
            return ResponseUtil.FAILURE("모집이 완료된 공고입니다.", null);
        ParticipationDetails participationDetails = participationDetailsService.checkParticipation(userId, postId);
        if(participationDetails != null && participationDetails.getStatus() == ParticipationDetailsStatus.JOIN)
            return ResponseUtil.FAILURE("이미 참가 신청이 되었습니다.", null);
        Long participationDetailsId = participationDetailsService.join(userId, postId);
        return ResponseUtil.SUCCESS("참가 신청이 완료되었습니다.", participationDetailsId);
    }

    //참가 취소 API
    @PostMapping("/{participationDetailsId}")
    public ResponseDto cancel(@PathVariable("participationDetailsId") Long participationDetailsId) {
        ParticipationDetails participationDetails = participationDetailsService.findById(participationDetailsId);
        postsService.cancelParticipation(participationDetails.getPost().getId());
        participationDetails.cancel();
        return ResponseUtil.SUCCESS("참가 취소가 완료되었습니다.", participationDetailsId);
    }

    //특정 유저 참가내역 전체 조회 API
    @GetMapping("/users/{userId}")
    public ResponseDto findAllDesc(@PathVariable("userId") Long userId) {
        return ResponseUtil.SUCCESS("참가내역 조회에 성공하였습니다.", participationDetailsService.findAllDesc(userId));
    }

}
