package com.moyeota.moyeotaproject.controller;

import com.moyeota.moyeotaproject.config.exception.ApiException;
import com.moyeota.moyeotaproject.config.exception.ErrorCode;
import com.moyeota.moyeotaproject.config.ResponseDto;
import com.moyeota.moyeotaproject.config.ResponseUtil;
import com.moyeota.moyeotaproject.controller.dto.PostsSaveRequestDto;
import com.moyeota.moyeotaproject.controller.dto.PostsUpdateRequestDto;
import com.moyeota.moyeotaproject.domain.posts.PostsStatus;
import com.moyeota.moyeotaproject.domain.posts.SameGender;
import com.moyeota.moyeotaproject.service.PostsService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    //모집글 작성 API
    @ApiOperation(value = "모집글 작성", notes = "특정 회원이 모집글을 작성하는 API")
    @PostMapping("/users/{userId}")
    public ResponseDto<Long> save(@ApiParam(value = "유저 인덱스 번호") @PathVariable("userId") Long userId, @RequestBody PostsSaveRequestDto requestDto){
        if(requestDto.getTitle() == null || requestDto.getTitle().equals(""))
            throw new ApiException(ErrorCode.POSTS_EMPTY_TITLE);

        if(requestDto.getDeparture() == null || requestDto.getDeparture().equals(""))
            throw new ApiException(ErrorCode.POSTS_EMPTY_DEPARTURE);

        if(requestDto.getDestination() == null || requestDto.getDestination().equals(""))
            throw new ApiException(ErrorCode.POSTS_EMPTY_DESTINATION);

        if(requestDto.getDepartureTime() == null || requestDto.getDepartureTime().equals(""))
            throw new ApiException(ErrorCode.POSTS_EMPTY_DEPARTURE_TIME);

        if(requestDto.getSameGenderStatus() == null)
            requestDto.setSameGenderStatus(SameGender.NO);

        Long postId = postsService.save(userId, requestDto);
        return ResponseUtil.SUCCESS("모집글 저장에 성공하였습니다.", postId);
    }

    //모집글 수정 API (단, 제목과 내용만 수정가능)
    @ApiOperation(value = "모집글 수정", notes = "특정 회원이 모집글을 수정하는 API")
    @PatchMapping("/{postId}")
    public ResponseDto update(@ApiParam(value = "모집글 인덱스 번호") @PathVariable("postId") Long postId, @RequestBody PostsUpdateRequestDto requestDto) {
        if(requestDto.getTitle() == null)
            requestDto.setTitle(postsService.findById(postId).getTitle());
        if(requestDto.getTitle().equals(""))
            throw new ApiException(ErrorCode.POSTS_EMPTY_TITLE);
        if(requestDto.getContent() == null)
            requestDto.setContent(postsService.findById(postId).getContent());

        return ResponseUtil.SUCCESS("모집글 수정에 성공하였습니다.", postsService.update(postId, requestDto));
    }

    //모집글 삭제 API
    @ApiOperation(value = "모집글 삭제", notes = "특정 회원이 모집글을 삭제하는 API")
    @DeleteMapping("/{postId}")
    public ResponseDto delete(@ApiParam(value = "모집글 인덱스 번호") @PathVariable("postId") Long postId) {
        postsService.delete(postId);
        return ResponseUtil.SUCCESS("모집글 삭제에 성공하였습니다.", postId);
    }

    //모집 마감 API
    @ApiOperation(value = "모집글 마감", notes = "특정 회원이 모집글을 마감하는 API")
    @PostMapping("/{postId}/complete")
    public ResponseDto completePost(@ApiParam(value = "모집글 인덱스 번호") @PathVariable("postId") Long postId) {
        if(postsService.findById(postId).getStatus() == PostsStatus.COMPLETE)
            throw new ApiException(ErrorCode.POSTS_ALREADY_FINISH);
        postsService.completePost(postId);
        return ResponseUtil.SUCCESS("모집글 공고가 마감되었습니다.", postId);
    }

    //특정 모집글 조회 API
    @ApiOperation(value = "특정 모집글 조회", notes = "특정 모집글을 조회하는 API")
    @GetMapping("/{postId}")
    public ResponseDto findById(@ApiParam(value = "모집글 인덱스 번호") @PathVariable("postId") Long postId) {
        return ResponseUtil.SUCCESS("모집글 조회에 성공하였습니다.", postsService.findById(postId));
    }

    //전체 모집글 조회 API (단, 상태가 RECRUITING 모집글만 조회 최신순으로)
    @ApiOperation(value = "모집글 전체 조회", notes = "모집글을 최신순으로 전체 조회하는 API")
    @GetMapping("")
    public ResponseDto findAllDesc() {
        return ResponseUtil.SUCCESS("모집글 조회에 성공하였습니다.", postsService.findAllDesc());
    }

    //내 모집글 목록 조회 API (최신순으로)
    @ApiOperation(value = "특정 회원 모집글 전체 조회", notes = "특정 회원의 모집글을 전체 조회하는 API")
    @GetMapping("users/{userId}")
    public ResponseDto findMyPostsByIdDesc(@ApiParam(value = "유저 인덱스 번호") @PathVariable("userId") Long userId) {
        return ResponseUtil.SUCCESS("모집글 조회에 성공하였습니다.", postsService.findMyPostsByIdDesc(userId));
    }

}
