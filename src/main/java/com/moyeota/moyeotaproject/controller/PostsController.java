package com.moyeota.moyeotaproject.controller;

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
    public Long save(@PathVariable("id") Long id, @RequestBody PostsSaveRequestDto requestDto){
        return postsService.save(id, requestDto);
    }

    @PutMapping("/{id}")
    public Long update(@PathVariable("id") Long id, @RequestBody PostsUpdateRequestDto requestDto) {
        return postsService.update(id, requestDto);
    }



}
