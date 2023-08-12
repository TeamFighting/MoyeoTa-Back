package com.moyeota.moyeotaproject.controller;

import com.moyeota.moyeotaproject.config.ResponseDto;
import com.moyeota.moyeotaproject.config.ResponseUtil;
import com.moyeota.moyeotaproject.controller.dto.PostsSaveRequestDto;
import com.moyeota.moyeotaproject.controller.dto.PostsUpdateRequestDto;
import com.moyeota.moyeotaproject.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostsController {

    private final PostsService postsService;

    @PostMapping("/{id}")
    public ResponseDto save(@PathVariable("id") Long id, @RequestBody PostsSaveRequestDto requestDto){
        Long postId = postsService.save(id, requestDto);
        return ResponseUtil.SUCCESS("모집글 저장에 성공하였습니다.", postId);
    }

    @PutMapping("/{id}")
    public ResponseDto update(@PathVariable("id") Long id, @RequestBody PostsUpdateRequestDto requestDto) {
        Long postId = postsService.update(id, requestDto);
        return ResponseUtil.SUCCESS("모집글 수정에 성공하였습니다.", postId);
    }

    @DeleteMapping("/{id}")
    public ResponseDto delete(@PathVariable("id") Long id) {
        postsService.delete(id);
        return ResponseUtil.SUCCESS("모집글 삭제에 성공하였습니다.", id);
    }

    @GetMapping("/{id}")
    public ResponseDto findById(@PathVariable("id") Long id) {
        return ResponseUtil.SUCCESS("모집글 조회에 성공하였습니다.", postsService.findById(id));
    }

    @GetMapping("")
    public ResponseDto findAllDesc() {
        return ResponseUtil.SUCCESS("모집글 조회에 성공하였습니다.", postsService.findAllDesc());
    }

}
