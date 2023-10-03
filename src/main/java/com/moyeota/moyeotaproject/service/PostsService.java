package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.controller.dto.postsDto.PostsResponseDto;
import com.moyeota.moyeotaproject.controller.dto.postsDto.PostsSaveRequestDto;
import com.moyeota.moyeotaproject.controller.dto.postsDto.PostsUpdateRequestDto;
import com.moyeota.moyeotaproject.domain.posts.Category;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.posts.PostsRepository;
import com.moyeota.moyeotaproject.domain.posts.PostsStatus;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class PostsService {

    private final UsersRepository usersRepository;
    private final PostsRepository postsRepository;

    @Transactional(readOnly = true)
    public Slice<PostsResponseDto> findAllDesc(Pageable pageable) {
        Slice<Posts> postsSlice = postsRepository.findAllByStatus(pageable, PostsStatus.RECRUITING);
        Slice<PostsResponseDto> postsResponseDtos = postsSlice.map(p -> new PostsResponseDto(p, p.getUser().getName(), p.getUser().getProfileImage(), p.getUser().getGender()));
        return postsResponseDtos;
    }

    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long postId) {
        Posts posts = postsRepository.findById(postId).orElseThrow(()
        -> new IllegalArgumentException("해당 게시글이 없습니다. id="+ postId));

        PostsResponseDto responseDto = PostsResponseDto.builder()
                .posts(posts)
                .userName(posts.getUser().getName())
                .profileImage(posts.getUser().getProfileImage())
                .userGender(posts.getUser().getGender()).build();

        return responseDto;
    }

    public Long save(Long userId, PostsSaveRequestDto requestDto){
//        Users users1 = Users.builder()
//                .name("kyko").profileImage("profile")
//                .phoneNumber("010-1111-1111")
//                .email("kyko@naver.com")
//                .loginId("loginId")
//                .password("password")
//                .status("join")
//                .gender(true)
//                .school("seoultech")
//                .averageStarRate(3.5F)
//                .isAuthenticated(true)
//                .provider(OAuthProvider.KAKAO)
//                .build();
//        usersRepository.save(users1); //제거예정

        Users user = usersRepository.findById(userId).orElseThrow(()
        -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));

        Posts post = requestDto.toEntity(user);
        return postsRepository.save(post).getId();
    }

    public Long update(Long postId, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(postId).orElseThrow(()
        -> new IllegalArgumentException("해당 모집글이 없습니다. id=" + postId));

        posts.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getCategory(), requestDto.getDeparture(), requestDto.getDestination(), requestDto.getDepartureTime(), requestDto.getSameGenderStatus(), requestDto.getVehicle(), requestDto.getNumberOfRecruitment(), requestDto.getFare(), requestDto.getDuration());
        return postId;
    }

    public void delete(Long postId) {
        Posts posts = postsRepository.findById(postId).orElseThrow(()
        -> new IllegalArgumentException("해당 모집글이 없습니다. id=" + postId));
        postsRepository.delete(posts);
    }

    public void completePost(Long postId) {
        Posts post = postsRepository.findById(postId).orElseThrow(()
        -> new IllegalArgumentException("해당 모집글이 없습니다. id=" + postId));
        post.postsComplete();
    }

    public Slice<PostsResponseDto> findMyPostsByIdDesc(Long userId, Pageable pageable) {
        Users user = usersRepository.findById(userId).orElseThrow(()
        -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));
        Slice<Posts> postsSlice = postsRepository.findByUser(user, pageable);
        Slice<PostsResponseDto> postsResponseDtos = postsSlice.map(p -> new PostsResponseDto(p, p.getUser().getName(), p.getUser().getProfileImage(), p.getUser().getGender()));
        return postsResponseDtos;
    }

    public void cancelParticipation(Long postId) {
        Posts post = postsRepository.findById(postId).orElseThrow(()
        -> new IllegalArgumentException("해당 모집글이 없습니다. id=" + postId));
        post.minusUser();
    }

    public Slice<PostsResponseDto> findAllByCategory(Category category, Pageable pageable) {
        Slice<Posts> postsSlice = postsRepository.findByCategory(category, PostsStatus.RECRUITING ,pageable);
        Slice<PostsResponseDto> postsResponseDtos = postsSlice.map(p -> new PostsResponseDto(p, p.getUser().getName(), p.getUser().getProfileImage(), p.getUser().getGender()));
        return postsResponseDtos;
    }

}
