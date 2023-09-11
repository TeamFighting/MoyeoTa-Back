package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.controller.dto.PostsResponseDto;
import com.moyeota.moyeotaproject.controller.dto.PostsSaveRequestDto;
import com.moyeota.moyeotaproject.controller.dto.PostsUpdateRequestDto;
import com.moyeota.moyeotaproject.domain.posts.Category;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.posts.PostsRepository;
import com.moyeota.moyeotaproject.domain.posts.PostsStatus;
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
            if(postsList.get(i).getStatus() == PostsStatus.RECRUITING) {
                PostsResponseDto responseDto = PostsResponseDto.builder()
                        .posts(postsList.get(i))
                        .userName(postsList.get(i).getUser().getName())
                        .profileImage(postsList.get(i).getUser().getProfileImage())
                        .userGender(postsList.get(i).getUser().getGender()).build();
                list.add(responseDto);
            }
        }
        return list;
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
//        Users users2 = Users.builder()
//                .name("kyko22").profileImage("profile")
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
//        usersRepository.save(users2);

        Users user = usersRepository.findById(userId).orElseThrow(()
        -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));

        return postsRepository.save(requestDto.toEntity(user)).getId();
    }

    public Long update(Long postId, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(postId).orElseThrow(()
        -> new IllegalArgumentException("해당 모집글이 없습니다. id=" + postId));

        posts.update(requestDto.getTitle(), requestDto.getContent());
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

    public List<PostsResponseDto> findMyPostsByIdDesc(Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow(()
        -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));
        List<Posts> postsList = postsRepository.findByUserOrderByCreatedDateDesc(user);
        List<PostsResponseDto> list = new ArrayList<>();
        for (int i=0; i<postsList.size(); i++){
            PostsResponseDto responseDto = PostsResponseDto.builder()
                    .posts(postsList.get(i))
                    .userName(postsList.get(i).getUser().getName())
                    .profileImage(postsList.get(i).getUser().getProfileImage())
                    .userGender(postsList.get(i).getUser().getGender()).build();
            list.add(responseDto);
        }
        return list;
    }

    public void cancelParticipation(Long postId) {
        Posts post = postsRepository.findById(postId).orElseThrow(()
        -> new IllegalArgumentException("해당 모집글이 없습니다. id=" + postId));
        post.minusUser();
    }

    public List<PostsResponseDto> findAllByCategoryDesc(Category category) {
        List<Posts> postsList = postsRepository.findByCategoryOrderByIdDesc(category);
        List<PostsResponseDto> list = new ArrayList<>();
        if(postsList.size() == 0)
            return list;
        for (int i=0; i<postsList.size(); i++){
            if(postsList.get(i).getStatus() == PostsStatus.RECRUITING) {
                PostsResponseDto responseDto = PostsResponseDto.builder()
                        .posts(postsList.get(i))
                        .userName(postsList.get(i).getUser().getName())
                        .profileImage(postsList.get(i).getUser().getProfileImage())
                        .userGender(postsList.get(i).getUser().getGender()).build();
                list.add(responseDto);
            }
        }
        return list;
    }
}
