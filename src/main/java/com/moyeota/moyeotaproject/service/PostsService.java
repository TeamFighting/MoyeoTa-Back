package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.controller.dto.PostsResponseDto;
import com.moyeota.moyeotaproject.controller.dto.PostsSaveRequestDto;
import com.moyeota.moyeotaproject.controller.dto.PostsUpdateRequestDto;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.posts.PostsRepository;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class PostsService {

    private final UsersRepository usersRepository;
    private final PostsRepository postsRepository;

    @Transactional(readOnly = true)
    public List<PostsResponseDto> findAllDesc() {
        List<Posts> postsList = postsRepository.findAllDesc();
        List<PostsResponseDto> list = new ArrayList<>();
        for (int i=0; i<postsList.size(); i++){
            PostsResponseDto responseDto = PostsResponseDto.builder()
                    .posts(postsList.get(i))
                    .userName(postsList.get(i).getUser().getName())
                    .profileImage(postsList.get(i).getUser().getProfileImage())
                    .userAverageStarRate(postsList.get(i).getUser().getAverageStarRate())
                    .userGender(postsList.get(i).getUser().getGender()).build();
            list.add(responseDto);
        }
        return list;
    }

    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(()
        -> new IllegalArgumentException("해당 게시글이 없습니다. id="+ id));

        PostsResponseDto responseDto = PostsResponseDto.builder()
                .posts(posts)
                .userName(posts.getUser().getName())
                .profileImage(posts.getUser().getProfileImage())
                .userAverageStarRate(posts.getUser().getAverageStarRate())
                .userGender(posts.getUser().getGender()).build();

        return responseDto;
    }

    public Long save(Long id, PostsSaveRequestDto requestDto){
        Users users = new Users( //제거예정
                "kyko", "profile", "010-1111-1111", "kyko@naver.com", "loginId",
                "password", "join", true, 3.5F, "seoultech", true
        );
        usersRepository.save(users); //제거예정
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

    public void delete(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(()
        -> new IllegalArgumentException("해당 모집글이 없습니다. id=" + id));
        postsRepository.delete(posts);
    }

}
