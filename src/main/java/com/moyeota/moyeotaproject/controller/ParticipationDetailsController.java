package com.moyeota.moyeotaproject.controller;

import com.moyeota.moyeotaproject.config.response.ResponseDto;
import com.moyeota.moyeotaproject.config.response.ResponseUtil;
import com.moyeota.moyeotaproject.config.exception.ApiException;
import com.moyeota.moyeotaproject.config.exception.ErrorCode;
import com.moyeota.moyeotaproject.controller.dto.participationDetailsDto.DistancePriceDto;
import com.moyeota.moyeotaproject.controller.dto.participationDetailsDto.ParticipationDetailsResponseDto;
import com.moyeota.moyeotaproject.controller.dto.postsDto.PostsResponseDto;
import com.moyeota.moyeotaproject.domain.participationDetails.ParticipationDetails;
import com.moyeota.moyeotaproject.domain.participationDetails.ParticipationDetailsStatus;
import com.moyeota.moyeotaproject.domain.posts.PostsStatus;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.service.ParticipationDetailsService;
import com.moyeota.moyeotaproject.service.PostsService;
import io.swagger.annotations.*;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    //참가 신청 API
    @ApiOperation(value = "참가 신청", notes = "특정 회원이 특정 모집글에 참가 신청 API(jwt토큰 필요)")
    @PostMapping("/join/posts/{postId}")
    public ResponseDto<Long> join(HttpServletRequest request,
                                  @ApiParam(value = "모집글 인덱스 번호") @PathVariable("postId") Long postId) {
        PostsResponseDto responseDto = postsService.findById(postId);
        if (responseDto.getNumberOfParticipants() == responseDto.getNumberOfRecruitment()) {
            throw new ApiException(ErrorCode.POSTS_ALREADY_FINISH);
        } else if (responseDto.getStatus() == PostsStatus.COMPLETE) {
            throw new ApiException(ErrorCode.POSTS_ALREADY_FINISH);
        }
        String accessToken = request.getHeader("Authorization");
        ParticipationDetails participationDetails = participationDetailsService.checkParticipation(accessToken, postId);
        if (participationDetails != null && participationDetails.getStatus() == ParticipationDetailsStatus.JOIN) {
            throw new ApiException(ErrorCode.PARTICIPATION_DETAILS_ALREADY_JOIN);
        }
        Users user = participationDetailsService.getUserByToken(accessToken);
        Long participationDetailsId = participationDetailsService.join(user.getId(), postId);
        return ResponseUtil.SUCCESS("참가 신청이 완료되었습니다.", participationDetailsId);
    }

    //참가 취소 API
    @ApiOperation(value = "참가 취소", notes = "참가 취소 API(jwt토큰 필요)")
    @PostMapping("/cancellation/posts/{postId}") //유저 인증 먼저 하기
    public ResponseDto<Long> cancel(HttpServletRequest request,
                                    @ApiParam(value = "참가내역 인덱스 번호") @PathVariable("postId") Long postId) {
        Users user = participationDetailsService.getUserByToken(request.getHeader("Authorization"));
        postsService.cancelParticipation(postId);
        participationDetailsService.cancelParticipation(postId, user);
        return ResponseUtil.SUCCESS("참가 취소가 완료되었습니다.", postId);
    }

    //특정 유저 이용기록 전체 조회 API
    @ApiOperation(value = "이용 기록 조회", notes = "특정 회원의 이용기록 조회 API")
    @GetMapping("/users/{userId}/all")
    public ResponseDto<List<ParticipationDetailsResponseDto>> findAllDesc(
            @ApiParam(value = "유저 인덱스 번호") @PathVariable("userId") Long userId) {
        return ResponseUtil.SUCCESS("이용기록 조회에 성공하였습니다.", participationDetailsService.findAllDesc(userId));
    }

    //내가 참가 신청한 모집글 목록 조회 API (최신순으로)
    @ApiOperation(value = "특정 회원의 참가 신청 모집글 전체 조회", notes = "특정 회원이 참가 신청한 모집글을 전체 조회하는 API")
    @GetMapping("/users/{userId}")
    public ResponseDto<List<PostsResponseDto>> findMyParticipationDetailsDesc(
            @ApiParam(value = "유저 인덱스 번호") @PathVariable("userId") Long userId) {
        return ResponseUtil.SUCCESS("모집글 조회에 성공하였습니다.",
                participationDetailsService.findMyParticipationDetailsDesc(userId));
    }

    @ApiOperation(value = "이용객의 이동거리 입력")
    @PostMapping("/posts/{postId}")
    public ResponseDto<Long> saveDistance(HttpServletRequest request,
                                          @ApiParam(value = "모집글 인덱스 번호") @PathVariable("postId") Long postId,
                                          @ApiParam(value = "이동 거리") @RequestParam("distance") double distance) {
        Users user = participationDetailsService.getUserByToken(request.getHeader("Authorization"));
        return ResponseUtil.SUCCESS("이동거리 저장에 성공하였습니다.",
                participationDetailsService.saveDistance(user, postId, distance));
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
