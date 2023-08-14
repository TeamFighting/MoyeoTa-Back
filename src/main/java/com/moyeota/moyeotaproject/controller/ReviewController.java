package com.moyeota.moyeotaproject.controller;

import com.moyeota.moyeotaproject.config.ResponseDto;
import com.moyeota.moyeotaproject.config.ResponseUtil;
import com.moyeota.moyeotaproject.controller.dto.ReviewSaveRequestDto;
import com.moyeota.moyeotaproject.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    //리뷰 생성 API
    @PostMapping("/{userId}")
    public ResponseDto save(@PathVariable("userId") Long userId, @RequestBody ReviewSaveRequestDto requestDto) {
        Long reviewId = reviewService.save(userId, requestDto);
        return ResponseUtil.SUCCESS("리뷰 작성에 성공하였습니다.", reviewId);
    }

    //특정 리뷰 삭제 API
    @DeleteMapping("/{reviewId}")
    public ResponseDto delete(@PathVariable("reviewId") Long reviewId) {
        reviewService.delete(reviewId);
        return ResponseUtil.SUCCESS("리뷰 삭제에 성공하였습니다.", reviewId);
    }

    //특정 유저 전체 리뷰 조회 API (최신순으로)
    @GetMapping("/users/{userId}")
    public ResponseDto findAllDesc() {
        return ResponseUtil.SUCCESS("리뷰 조회에 성공하였습니다.", reviewService.findAllDesc());
    }

}
