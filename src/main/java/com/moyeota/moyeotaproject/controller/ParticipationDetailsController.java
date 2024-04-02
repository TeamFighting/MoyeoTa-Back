package com.moyeota.moyeotaproject.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moyeota.moyeotaproject.config.exception.ApiException;
import com.moyeota.moyeotaproject.config.exception.ErrorCode;
import com.moyeota.moyeotaproject.config.response.ResponseDto;
import com.moyeota.moyeotaproject.config.response.ResponseUtil;
import com.moyeota.moyeotaproject.dto.participationdetailsdto.DistancePriceDto;
import com.moyeota.moyeotaproject.dto.participationdetailsdto.ParticipationDetailsResponseDto;
import com.moyeota.moyeotaproject.dto.postsdto.PostsGetResponseDto;
import com.moyeota.moyeotaproject.domain.participationdetails.ParticipationDetails;
import com.moyeota.moyeotaproject.domain.participationdetails.ParticipationDetailsStatus;
import com.moyeota.moyeotaproject.domain.posts.PostsStatus;
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
	public ResponseDto<Long> join(HttpServletRequest request,
		@ApiParam(value = "모집글 인덱스 번호") @PathVariable("postId") Long postId) {
		PostsGetResponseDto responseDto = postsService.findById(postId);
		if (responseDto.getNumberOfParticipants() == responseDto.getNumberOfRecruitment()) {
			throw new ApiException(ErrorCode.POSTS_ALREADY_FINISH);
		} else if (responseDto.getStatus() == PostsStatus.COMPLETE) {
			throw new ApiException(ErrorCode.POSTS_ALREADY_FINISH);
		}

		ParticipationDetails participationDetails = participationDetailsService.checkParticipation(
			request.getHeader("Authorization"), postId);
		if (participationDetails != null && participationDetails.getStatus() == ParticipationDetailsStatus.JOIN) {
			throw new ApiException(ErrorCode.PARTICIPATION_DETAILS_ALREADY_JOIN);
		}
		Long participationDetailsId = participationDetailsService.join(request.getHeader("Authorization"), postId);
		return ResponseUtil.SUCCESS("참가 신청이 완료되었습니다.", participationDetailsId);
	}

	@ApiOperation(value = "참가 취소", notes = "참가 취소 API(jwt토큰 필요)")
	@PostMapping("/cancellation/posts/{postId}") //유저 인증 먼저 하기
	public ResponseDto<Long> cancel(HttpServletRequest request,
		@ApiParam(value = "참가내역 인덱스 번호") @PathVariable("postId") Long postId) {
		postsService.cancelParticipation(postId);
		participationDetailsService.cancelParticipation(postId, request.getHeader("Authorization"));
		return ResponseUtil.SUCCESS("참가 취소가 완료되었습니다.", postId);
	}

	@ApiOperation(value = "이용 기록 조회", notes = "특정 회원의 이용기록 조회 API")
	@GetMapping("/all")
	public ResponseDto<List<ParticipationDetailsResponseDto>> findAllDesc(HttpServletRequest request) {
		return ResponseUtil.SUCCESS("이용기록 조회에 성공하였습니다.",
			participationDetailsService.findAllDesc(request.getHeader("Authorization")));
	}

	@ApiOperation(value = "내가 신청한 팟 목록 조회", notes = "특정 회원이 참가 신청한 모집글을 전체 조회하는 API")
	@GetMapping("/")
	public ResponseDto<List<PostsGetResponseDto>> findMyParticipationDetailsDesc(HttpServletRequest request) {
		return ResponseUtil.SUCCESS("모집글 조회에 성공하였습니다.",
			participationDetailsService.findMyParticipationDetailsDesc(request.getHeader("Authorization")));
	}

	@ApiOperation(value = "이용객의 이동거리 입력")
	@PostMapping("/posts/{postId}")
	public ResponseDto<Long> saveDistance(HttpServletRequest request,
		@ApiParam(value = "모집글 인덱스 번호") @PathVariable("postId") Long postId,
		@ApiParam(value = "이동 거리") @RequestParam("distance") double distance) {
		return ResponseUtil.SUCCESS("이동거리 저장에 성공하였습니다.",
			participationDetailsService.saveDistance(request.getHeader("Authorization"), postId, distance));
	}

	@ApiOperation(value = "이용객의 이동거리 및 금액 조회")
	@GetMapping("/distance-payment/users/{userId}/posts/{postId}")
	public ResponseDto<DistancePriceDto> findDistanceAndPrice(
		@ApiParam(value = "유저 인덱스 번호") @PathVariable("userId") Long userId,
		@ApiParam(value = "모집글 인덱스 번호") @PathVariable("postId") Long postId) {
		return ResponseUtil.SUCCESS("이동거리 및 금액 조회에 성공하였습니다.",
			participationDetailsService.findDistanceAndPrice(userId, postId));
	}

}
