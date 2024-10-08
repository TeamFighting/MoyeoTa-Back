package com.moyeota.moyeotaproject.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moyeota.moyeotaproject.config.response.ResponseDto;
import com.moyeota.moyeotaproject.config.response.ResponseUtil;
import com.moyeota.moyeotaproject.dto.participationdetailsdto.DistancePriceDto;
import com.moyeota.moyeotaproject.dto.postsdto.PostsResponseDto;
import com.moyeota.moyeotaproject.service.ParticipationDetailsService;
import com.moyeota.moyeotaproject.service.PostsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@Api(tags = "ParticipationDetails", description = "ParticipationDetails Controller")
@ApiResponses({
	@ApiResponse(code = 200, message = "API 정상 작동"),
	@ApiResponse(code = 400, message = "BAD REQUEST"),
	@ApiResponse(code = 404, message = "NOT FOUND"),
	@ApiResponse(code = 500, message = "INTERNAL SERVER ERROR")
})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/participation-details")
public class ParticipationDetailsController {

	private final ParticipationDetailsService participationDetailsService;
	private final PostsService postsService;

	@ApiOperation(value = "참가 신청", notes = "특정 회원이 특정 모집글에 참가 신청 API(jwt토큰 필요)")
	@PostMapping("/join/posts/{postId}")
	public ResponseDto<Object> join(HttpServletRequest request,
		@ApiParam(value = "모집글 인덱스 번호") @PathVariable("postId") Long postId) {
		Object participationDetailsId = participationDetailsService.join(request.getHeader("Authorization"), postId);
		return ResponseUtil.SUCCESS("참가 신청이 완료되었습니다.", participationDetailsId);
	}

	@ApiOperation(value = "참가 취소", notes = "참가 취소 API(jwt토큰 필요)")
	@PostMapping("/cancellation/posts/{postId}") //유저 인증 먼저 하기
	public ResponseDto<Long> cancel(HttpServletRequest request,
		@ApiParam(value = "참가내역 인덱스 번호") @PathVariable("postId") Long postId) {
		participationDetailsService.cancelParticipation(postId, request.getHeader("Authorization"), postsService);
		return ResponseUtil.SUCCESS("참가 취소가 완료되었습니다.", postId);
	}

	@ApiOperation(value = "이용 기록 조회", notes = "특정 회원의 이용기록 조회 API")
	@GetMapping("/all")
	public ResponseDto<List<PostsResponseDto>> findAllDesc(HttpServletRequest request) {
		return ResponseUtil.SUCCESS("이용기록 조회에 성공하였습니다.",
			participationDetailsService.findAllDesc(request.getHeader("Authorization")));
	}

	@ApiOperation(value = "내가 신청한 팟 목록 조회", notes = "특정 회원이 참가 신청한 모집글을 전체 조회하는 API")
	@GetMapping("/")
	public ResponseDto<List<PostsResponseDto>> findMyParticipationDetailsDesc(HttpServletRequest request) {
		return ResponseUtil.SUCCESS("모집글 조회에 성공하였습니다.",
			participationDetailsService.findMyParticipationDetailsDesc(request.getHeader("Authorization")));
	}

	@ApiOperation(value = "이용객의 하차 요금 입력")
	@PostMapping("/users/{userId}/posts/{postId}")
	public ResponseDto<Long> saveDistance(HttpServletRequest request,
		@ApiParam(value = "유저 인덱스 번호") @PathVariable("userId") Long userId,
		@ApiParam(value = "모집글 인덱스 번호") @PathVariable("postId") Long postId,
		@ApiParam(value = "하차 요금") @RequestParam("fare") double fare) {
		return ResponseUtil.SUCCESS("하차 요금 저장에 성공하였습니다.",
			participationDetailsService.savePrice(request.getHeader("Authorization"), userId, postId, fare));
	}

	@ApiOperation(value = "이용객의 금액 조회")
	@GetMapping("/payment/users/{userId}/posts/{postId}")
	public ResponseDto<DistancePriceDto> findDistanceAndPrice(
		@ApiParam(value = "유저 인덱스 번호") @PathVariable("userId") Long userId,
		@ApiParam(value = "모집글 인덱스 번호") @PathVariable("postId") Long postId) {
		return ResponseUtil.SUCCESS("이동거리 및 금액 조회에 성공하였습니다.",
			participationDetailsService.findDistanceAndPrice(userId, postId));
	}

	@ApiOperation(value = "정산 완료")
	@PatchMapping("/payment")
	public ResponseDto<Long> payment(HttpServletRequest request) {
		return ResponseUtil.SUCCESS("정산 완료로 변경", participationDetailsService.payment(request.getHeader("Authorization")));
	}

}
