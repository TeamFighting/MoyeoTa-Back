package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.controller.dto.PostsSaveRequestDto;
import com.moyeota.moyeotaproject.controller.dto.PostsUpdateRequestDto;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.posts.PostsRepository;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final UsersRepository usersRepository;

    public Long save(Long id, PostsSaveRequestDto requestDto){
        Users user = usersRepository.findById(id).orElseThrow(()
        -> new IllegalArgumentException("해당 유저가 없습니다. id=" + id));

        return postsRepository.save(requestDto.toEntity(user)).getId();
    }

    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(()
        -> new IllegalArgumentException("해당 모집글이 없습니다. id=" + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

}
