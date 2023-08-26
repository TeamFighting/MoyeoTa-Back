package com.moyeota.moyeotaproject.controller;

import com.moyeota.moyeotaproject.config.ResponseDto;
import com.moyeota.moyeotaproject.config.ResponseUtil;
import com.moyeota.moyeotaproject.controller.dto.PostsResponseDto;
import com.moyeota.moyeotaproject.controller.dto.PostsSaveRequestDto;
import com.moyeota.moyeotaproject.controller.dto.PostsUpdateRequestDto;
import com.moyeota.moyeotaproject.domain.posts.PostsStatus;
import com.moyeota.moyeotaproject.domain.posts.SameGender;
import com.moyeota.moyeotaproject.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostsController {

    private final PostsService postsService;

    //모집글 작성 API
    @PostMapping("/{userId}")
    public ResponseDto save(@PathVariable("userId") Long userId, @RequestBody PostsSaveRequestDto requestDto){
        if(requestDto.getTitle() == null || requestDto.getTitle().equals(""))
            return ResponseUtil.FAILURE("모집글 제목을 입력해주세요.", null);
        if(requestDto.getDeparture() == null || requestDto.getDeparture().equals(""))
            return ResponseUtil.FAILURE("출발지를 입력해주세요.", null);
        if(requestDto.getDestination() == null || requestDto.getDestination().equals(""))
            return ResponseUtil.FAILURE("목적지를 입력해주세요.", null);
        if(requestDto.getSameGenderStatus() == null)
            requestDto.setSameGenderStatus(SameGender.NO);

        Long postId = postsService.save(userId, requestDto);
        return ResponseUtil.SUCCESS("모집글 저장에 성공하였습니다.", postId);
    }

    //모집글 수정 API (단, 제목과 내용만 수정가능)
    @PatchMapping("/{postId}")
    public ResponseDto update(@PathVariable("postId") Long postId, @RequestBody PostsUpdateRequestDto requestDto) {
        if(requestDto.getTitle() == null)
            requestDto.setTitle(postsService.findById(postId).getTitle());
        if(requestDto.getTitle().equals(""))
            return ResponseUtil.FAILURE("모집글 제목을 입력해주세요.", null);
        if(requestDto.getContent() == null)
            requestDto.setContent(postsService.findById(postId).getContent());

        return ResponseUtil.SUCCESS("모집글 수정에 성공하였습니다.", postsService.update(postId, requestDto));
    }

    //모집글 삭제 API
    @DeleteMapping("/{postId}")
    public ResponseDto delete(@PathVariable("postId") Long postId) {
        postsService.delete(postId);
        return ResponseUtil.SUCCESS("모집글 삭제에 성공하였습니다.", postId);
    }

    //모집 마감 API
    @PostMapping("/{postId}/completion")
    public ResponseDto completePost(@PathVariable("postId") Long postId) {
        if(postsService.findById(postId).getStatus() == PostsStatus.COMPLETE)
            return ResponseUtil.FAILURE("이미 마감된 공고입니다.", null);
        postsService.completePost(postId);
        return ResponseUtil.SUCCESS("모집글 공고가 마감되었습니다.", postId);
    }

    //특정 모집글 조회 API
    @GetMapping("/{postId}")
    public ResponseDto findById(@PathVariable("postId") Long postId) {
        return ResponseUtil.SUCCESS("모집글 조회에 성공하였습니다.", postsService.findById(postId));
    }

    //전체 모집글 조회 API (단, 상태가 RECRUITING 모집글만 조회 최신순으로)
    @GetMapping("")
    public ResponseDto findAllDesc() {
        return ResponseUtil.SUCCESS("모집글 조회에 성공하였습니다.", postsService.findAllDesc());
    }

    //내 모집글 목록 조회 API (최신순으로)
    @GetMapping("users/{userId}")
    public ResponseDto findMyPostsByIdDesc(@PathVariable("userId") Long userId) {
        return ResponseUtil.SUCCESS("모집글 조회에 성공하였습니다.", postsService.findMyPostsByIdDesc(userId));
    }

}
