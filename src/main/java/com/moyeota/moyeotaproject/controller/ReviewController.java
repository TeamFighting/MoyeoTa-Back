package com.moyeota.moyeotaproject.controller;

import com.moyeota.moyeotaproject.config.response.ResponseDto;
import com.moyeota.moyeotaproject.config.response.ResponseUtil;
import com.moyeota.moyeotaproject.controller.dto.reviewDto.ReviewResponseDto;
import com.moyeota.moyeotaproject.controller.dto.reviewDto.ReviewSaveRequestDto;
import com.moyeota.moyeotaproject.service.ReviewService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "Reviews", description = "Review Controller")
@ApiResponses({
        @ApiResponse(code = 200, message = "API 정상 작동"),
        @ApiResponse(code = 400, message = "BAD REQUEST"),
        @ApiResponse(code = 404, message = "NOT FOUND"),
        @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR")
})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    //리뷰 작성 API
    @ApiOperation(value = "리뷰 작성", notes = "특정 회원의 리뷰 작성 API")
    @PostMapping("/users/{userId}")
    public ResponseDto save(HttpServletRequest request, @ApiParam(value = "평가받는 유저 인덱스 번호") @PathVariable("userId") Long userId, @RequestBody ReviewSaveRequestDto requestDto) {
        Long reviewId = reviewService.save(request.getHeader("Authorization"), userId, requestDto);
        return ResponseUtil.SUCCESS("리뷰 작성에 성공하였습니다.", reviewId);
    }

    //특정 리뷰 삭제 API
    @ApiOperation(value = "리뷰 삭제", notes = "특정 회원의 리뷰 삭제 API")
    @DeleteMapping("/{reviewId}/users/{userId}") //users/{userId}를 추가해서 jwt 인증을 먼저 해야할듯
    public ResponseDto delete(HttpServletRequest request, @ApiParam(value = "리뷰 인덱스 번호") @PathVariable("reviewId") Long reviewId, @ApiParam(value = "유저 인덱스 번호") @PathVariable("userId") Long userId) {
        reviewService.delete(request.getHeader("Authorization"), reviewId);
        return ResponseUtil.SUCCESS("리뷰 삭제에 성공하였습니다.", reviewId);
    }

    //특정 유저 전체 리뷰 조회 API (최신순으로)
    @ApiOperation(value = "리뷰 전체 조회", notes = "특정 회원의 리뷰 최신순으로 전체 조회 API")
    @GetMapping("/users/{userId}")
    public ResponseDto<Slice<ReviewResponseDto>> findAllDesc(@ApiParam(value = "페이지 번호(0부터 시작)") @RequestParam("page") int page, @ApiParam(value = "유저 인덱스 번호") @PathVariable("userId") Long userId) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        return ResponseUtil.SUCCESS("리뷰 조회에 성공하였습니다.", reviewService.findAllDesc(userId, pageable));
    }

}
