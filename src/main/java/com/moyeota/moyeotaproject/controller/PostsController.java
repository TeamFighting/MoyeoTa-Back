package com.moyeota.moyeotaproject.controller;

import com.moyeota.moyeotaproject.config.exception.ApiException;
import com.moyeota.moyeotaproject.config.exception.ErrorCode;
import com.moyeota.moyeotaproject.config.response.ResponseDto;
import com.moyeota.moyeotaproject.config.response.ResponseUtil;
import com.moyeota.moyeotaproject.controller.dto.postsDto.PostsMemberDto;
import com.moyeota.moyeotaproject.controller.dto.postsDto.PostsResponseDto;
import com.moyeota.moyeotaproject.controller.dto.postsDto.PostsSaveRequestDto;
import com.moyeota.moyeotaproject.controller.dto.postsDto.PostsUpdateRequestDto;
import com.moyeota.moyeotaproject.domain.posts.Category;
import com.moyeota.moyeotaproject.domain.posts.PostsStatus;
import com.moyeota.moyeotaproject.domain.posts.SameGender;
import com.moyeota.moyeotaproject.domain.posts.Vehicle;
import com.moyeota.moyeotaproject.service.ParticipationDetailsService;
import com.moyeota.moyeotaproject.service.PostsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Posts", description = "Post Controller")
@ApiResponses({
        @ApiResponse(code = 200, message = "API 정상 작동"),
        @ApiResponse(code = 400, message = "BAD REQUEST"),
        @ApiResponse(code = 404, message = "NOT FOUND"),
        @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR")
})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostsController {

    private final PostsService postsService;
    private final ParticipationDetailsService participationDetailsService;

    //모집글 작성 API
    @ApiOperation(value = "모집글 작성", notes = "특정 회원이 모집글을 작성하는 API(jwt토큰 필요)")
    @PostMapping("")
    public ResponseDto<Long> save(HttpServletRequest request, @RequestBody PostsSaveRequestDto requestDto) {
        if (requestDto.getTitle() == null || requestDto.getTitle().equals("")) {
            throw new ApiException(ErrorCode.POSTS_EMPTY_TITLE);
        }

        if (requestDto.getDeparture() == null || requestDto.getDeparture().equals("")) {
            throw new ApiException(ErrorCode.POSTS_EMPTY_DEPARTURE);
        }

        if (requestDto.getDestination() == null || requestDto.getDestination().equals("")) {
            throw new ApiException(ErrorCode.POSTS_EMPTY_DESTINATION);
        }

        if (requestDto.getDepartureTime() == null || requestDto.getDepartureTime().equals("")) {
            throw new ApiException(ErrorCode.POSTS_EMPTY_DEPARTURE_TIME);
        }

        if (requestDto.getVehicle() == null) {
            requestDto.setVehicle(Vehicle.일반);
        }

        if (requestDto.getSameGenderStatus() == null) {
            requestDto.setSameGenderStatus(SameGender.NO);
        }

        Long postId = postsService.save(request.getHeader("Authorization"), requestDto);
        return ResponseUtil.SUCCESS("모집글 저장에 성공하였습니다.", postId);
    }

    //모집글 수정 API (단, 제목과 내용만 수정가능)
    @ApiOperation(value = "모집글 수정", notes = "특정 회원이 모집글을 수정하는 API(jwt토큰 필요)")
    @PatchMapping("/{postId}")
    public ResponseDto update(HttpServletRequest request,
                              @ApiParam(value = "모집글 인덱스 번호") @PathVariable("postId") Long postId,
                              @RequestBody PostsUpdateRequestDto requestDto) {
        PostsResponseDto post = postsService.findById(postId);
        if (requestDto.getTitle() == null) {
            requestDto.setTitle(post.getTitle());
        }
        if (requestDto.getTitle().equals("")) {
            throw new ApiException(ErrorCode.POSTS_EMPTY_TITLE);
        }

        if (requestDto.getContent() == null) {
            requestDto.setContent(post.getContent());
        }

        if (requestDto.getCategory() == null) {
            requestDto.setCategory(post.getCategory());
        }

        if (requestDto.getDeparture() == null) {
            requestDto.setDeparture(post.getDeparture());
        }
        if (requestDto.getDeparture().equals("")) {
            throw new ApiException(ErrorCode.POSTS_EMPTY_DEPARTURE);
        }

        if (requestDto.getDestination() == null) {
            requestDto.setDestination(post.getDestination());
        }
        if (requestDto.getDestination().equals("")) {
            throw new ApiException(ErrorCode.POSTS_EMPTY_DESTINATION);
        }

        if (requestDto.getDepartureTime() == null) {
            requestDto.setDepartureTime(post.getDepartureTime());
        }

        if (requestDto.getSameGenderStatus() == null) {
            requestDto.setSameGenderStatus(post.getSameGenderStatus());
        }

        if (requestDto.getFare() == 0) {
            requestDto.setFare(post.getFare());
        }

        if (requestDto.getDuration() == 0) {
            requestDto.setDuration(post.getDuration());
        }

        if (requestDto.getDistance() == 0) {
            requestDto.setDistance(post.getDistance());
        }

        if (requestDto.getNumberOfRecruitment() == 0) {
            requestDto.setNumberOfRecruitment(post.getNumberOfRecruitment());
        }

        if (requestDto.getVehicle() == null) {
            requestDto.setVehicle(post.getVehicle());
        }

        return ResponseUtil.SUCCESS("모집글 수정에 성공하였습니다.",
                postsService.update(request.getHeader("Authorization"), postId, requestDto));
    }

    //모집글 삭제 API
    @ApiOperation(value = "모집글 삭제", notes = "특정 회원이 모집글을 삭제하는 API(jwt토큰 필요)")
    @DeleteMapping("/{postId}")
    public ResponseDto delete(HttpServletRequest request,
                              @ApiParam(value = "모집글 인덱스 번호") @PathVariable("postId") Long postId) {
        postsService.delete(request.getHeader("Authorization"), postId);
        return ResponseUtil.SUCCESS("모집글 삭제에 성공하였습니다.", postId);
    }

    //모집 마감 API
    @ApiOperation(value = "모집글 마감", notes = "특정 회원이 모집글을 마감하는 API(jwt토큰 필요)")
    @PostMapping("/{postId}/complete")
    public ResponseDto completePost(HttpServletRequest request,
                                    @ApiParam(value = "모집글 인덱스 번호") @PathVariable("postId") Long postId) {
        postsService.getUserByToken(request.getHeader("Authorization"));
        if (postsService.findById(postId).getStatus() == PostsStatus.COMPLETE) {
            throw new ApiException(ErrorCode.POSTS_ALREADY_FINISH);
        }
        postsService.completePost(postId);
        return ResponseUtil.SUCCESS("모집글 공고가 마감되었습니다.", postId);
    }

    //특정 모집글 조회 API
    @ApiOperation(value = "특정 모집글 조회", notes = "특정 모집글을 조회하는 API")
    @GetMapping("/{postId}")
    public ResponseDto<PostsResponseDto> findById(@ApiParam(value = "모집글 인덱스 번호") @PathVariable("postId") Long postId) {
        return ResponseUtil.SUCCESS("모집글 조회에 성공하였습니다.", postsService.findById(postId));
    }


    //전체 모집글 조회 API (단, 상태가 RECRUITING 모집글만 조회 최신순으로)
    @ApiOperation(value = "모집글 전체 조회", notes = "모집글을 최신순으로 전체 조회하는 API")
    @GetMapping("")
    public ResponseDto<Slice<PostsResponseDto>> findAllDesc(@RequestParam("page") int page) {
        Pageable pageable = PageRequest.of(page, 3, Sort.by("id").descending());
        return ResponseUtil.SUCCESS("모집글 조회에 성공하였습니다.", postsService.findAllDesc(pageable));
    }

    //내 모집글 목록 조회 API (최신순으로)
    @ApiOperation(value = "특정 회원 모집글 전체 조회", notes = "특정 회원의 모집글을 전체 조회하는 API")
    @GetMapping("/users/{userId}")
    public ResponseDto<Slice<PostsResponseDto>> findMyPostsByIdDesc(
            @ApiParam(value = "유저 인덱스 번호") @PathVariable("userId") Long userId,
            @ApiParam(value = "페이지 번호(0부터 시작)") @RequestParam("page") int page) {
        Pageable pageable = PageRequest.of(page, 3, Sort.by("id").descending());
        return ResponseUtil.SUCCESS("모집글 조회에 성공하였습니다.", postsService.findMyPostsByIdDesc(userId, pageable));
    }

    //카테고리별 모집글 조회 API (최신순으로)
    @ApiOperation(value = "카테고리별 모집글 전체 조회", notes = "특정 카테고리의 모집글 전체 최신순으로 조회하는 API")
    @GetMapping("/search")
    public ResponseDto<Slice<PostsResponseDto>> findAllByCategoryDesc(@RequestParam("category") Category category,
                                                                      @ApiParam(value = "페이지 번호(0부터 시작)") @RequestParam("page") int page) {
        Pageable pageable = PageRequest.of(page, 3, Sort.by("id").descending());
        return ResponseUtil.SUCCESS("모집글 조회에 성공하였습니다.", postsService.findAllByCategory(category, pageable));
    }

    //파티원 목록 조회 API
    @ApiOperation(value = "모집글 파티원 목록 조회", notes = "특정 모집글 파티원 목록을 조회하는 API(본인 포함)")
    @GetMapping("/{postId}/members")
    public ResponseDto<List<PostsMemberDto>> findPostsMembers(
            @ApiParam(value = "모집글 인덱스") @PathVariable("postId") Long postId) {
        return ResponseUtil.SUCCESS("모집글 파티원 조회에 성공하였습니다.", postsService.findPostsMembers(postId));
    }

}
