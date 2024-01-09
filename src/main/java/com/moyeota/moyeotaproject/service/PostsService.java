package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.config.jwtConfig.JwtTokenProvider;
import com.moyeota.moyeotaproject.controller.dto.postsDto.PostsMemberDto;
import com.moyeota.moyeotaproject.controller.dto.postsDto.PostsResponseDto;
import com.moyeota.moyeotaproject.controller.dto.postsDto.PostsSaveRequestDto;
import com.moyeota.moyeotaproject.controller.dto.postsDto.PostsUpdateRequestDto;
import com.moyeota.moyeotaproject.domain.participationDetails.ParticipationDetails;
import com.moyeota.moyeotaproject.domain.participationDetails.ParticipationDetailsRepository;
import com.moyeota.moyeotaproject.domain.posts.Category;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.posts.PostsRepository;
import com.moyeota.moyeotaproject.domain.posts.PostsStatus;
import com.moyeota.moyeotaproject.domain.totalDetail.TotalDetail;
import com.moyeota.moyeotaproject.domain.totalDetail.TotalDetailRepository;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class PostsService {

    private final ParticipationDetailsService participationDetailsService;
    private final UsersRepository usersRepository;
    private final PostsRepository postsRepository;
    private final ParticipationDetailsRepository participationDetailsRepository;
    private final TotalDetailRepository totalDetailRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(readOnly = true)
    public List<PostsMemberDto> findPostsMembers(Long postId) {
        List<ParticipationDetails> participationDetails = participationDetailsRepository.findParticipationDetailsByPostsId(
                postId);
        List<PostsMemberDto> postsMemberDtos = new ArrayList<>();
        for (int i = 0; i < participationDetails.size(); i++) {
            postsMemberDtos.add(PostsMemberDto.builder().user(participationDetails.get(i).getUser()).build());
        }
        return postsMemberDtos;
    }

    @Transactional(readOnly = true)
    public Slice<PostsResponseDto> findAllDesc(Pageable pageable) {
        Slice<Posts> postsSlice = postsRepository.findAllByStatus(pageable, PostsStatus.RECRUITING);
        Slice<PostsResponseDto> postsResponseDtos = postsSlice.map(
                p -> new PostsResponseDto(p, p.getUser().getName(), p.getUser().getProfileImage(),
                        p.getUser().getGender()));
        return postsResponseDtos;
    }

    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long postId) {
        updateView(postId);
        Posts posts = postsRepository.findById(postId).orElseThrow(()
                -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + postId));
        PostsResponseDto responseDto = PostsResponseDto.builder()
                .posts(posts)
                .userName(posts.getUser().getName())
                .profileImage(posts.getUser().getProfileImage())
                .userGender(posts.getUser().getGender()).build();

        return responseDto;
    }

    public Long save(String accessToken, PostsSaveRequestDto requestDto) {
        Users user = getUserByToken(accessToken);
        Posts post = requestDto.toEntity(user);
        System.out.println(post.getStatus());
        Long postId = postsRepository.save(post).getId();
        participationDetailsService.join(user.getId(), postId);
        return postId;
    }

    public Long update(String accessToken, Long postId, PostsUpdateRequestDto requestDto) {
        getUserByToken(accessToken);
        Posts posts = postsRepository.findById(postId).orElseThrow(()
                -> new IllegalArgumentException("해당 모집글이 없습니다. id=" + postId));

        posts.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getCategory(),
                requestDto.getDeparture(), requestDto.getDestination(), requestDto.getDepartureTime(),
                requestDto.getSameGenderStatus(), requestDto.getVehicle(), requestDto.getNumberOfRecruitment(),
                requestDto.getFare(), requestDto.getDuration(), requestDto.getDistance());
        return postId;
    }

    public void delete(String accessToken, Long postId) {
        getUserByToken(accessToken);
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
        Slice<PostsResponseDto> postsResponseDtos = postsSlice.map(
                p -> new PostsResponseDto(p, p.getUser().getName(), p.getUser().getProfileImage(),
                        p.getUser().getGender()));
        return postsResponseDtos;
    }

    public void cancelParticipation(Long postId) {
        Posts post = postsRepository.findById(postId).orElseThrow(()
                -> new IllegalArgumentException("해당 모집글이 없습니다. id=" + postId));
        post.minusUser();
    }

    public Slice<PostsResponseDto> findAllByCategory(Category category, Pageable pageable) {
        Slice<Posts> postsSlice = postsRepository.findByCategory(category, PostsStatus.RECRUITING, pageable);
        Slice<PostsResponseDto> postsResponseDtos = postsSlice.map(
                p -> new PostsResponseDto(p, p.getUser().getName(), p.getUser().getProfileImage(),
                        p.getUser().getGender()));
        return postsResponseDtos;
    }

    public Users getUserByToken(String accessToken) {
        Optional<Users> users = usersRepository.findById(jwtTokenProvider.extractSubjectFromJwt(accessToken));
        if (users.isPresent()) {
            return users.get();
        } else {
            throw new RuntimeException("토큰에 해당하는 멤버가 없습니다.");
        }
    }

    @Transactional
    public int updateView(Long id) {
        return postsRepository.updateView(id);
    }

    public Long calcPrice(Long postId) {
        List<ParticipationDetails> list = participationDetailsRepository.findParticipationDetailsByPostsId(postId);
        TotalDetail totalDetail = totalDetailRepository.findByPostId(postId).orElseThrow(()
                -> new IllegalArgumentException("해당 내역이 없습니다. id=" + postId));

        double sum = 0;
        double totalPayment = totalDetail.getTotalPayment();
        for (int i=0; i<list.size(); i++) {
            sum += list.get(i).getDistance();
        }

        for (int i=0; i<list.size(); i++) {
            ParticipationDetails participationDetails = list.get(i);
            participationDetails.updatePrice((participationDetails.getDistance() / sum) * totalPayment);
        }

        return postId;
    }
}
