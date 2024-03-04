package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.config.jwtConfig.JwtTokenProvider;
import com.moyeota.moyeotaproject.controller.dto.postsDto.PostsMemberDto;
import com.moyeota.moyeotaproject.domain.chatRoom.ChatRoomRepository;
import com.moyeota.moyeotaproject.domain.participationDetails.ParticipationDetails;
import com.moyeota.moyeotaproject.domain.participationDetails.ParticipationDetailsRepository;
import com.moyeota.moyeotaproject.domain.posts.*;
import com.moyeota.moyeotaproject.domain.totalDetail.TotalDetailRepository;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;
import org.apache.catalina.User;
import org.assertj.core.api.Assertions;
import org.joda.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.parameters.P;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PostsServiceTest {
    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ParticipationDetailsService participationDetailsService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private PostsRepository postsRepository;

    @Mock
    private ParticipationDetailsRepository participationDetailsRepository;

    @Mock
    private TotalDetailRepository totalDetailRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("게시글_작성_유저_찾기")
    void findPostsMembers() {
        // Given
        PostsService postsService = new PostsService(chatRoomRepository, participationDetailsService, usersRepository, postsRepository
                , participationDetailsRepository, totalDetailRepository, jwtTokenProvider);
        Users users = Users.builder()
                .loginId("tae77777")
                .password("rlaxogjs8312")
                .email("tae77777@naver.com")
                .build();
        usersRepository.save(users);
        when(usersRepository.save(any())).thenReturn(users); // usersRepository.save() 호출 시 Mock 객체의 반환 설정
        // assertThat(usersRepository.findAll()).hasSize(1);

        Posts posts = Posts.builder()
                .title("제목")
                .category(Category.COLLEGE)
                .departure("출발지")
                .destination("도착지")
                .departureTime(null)
                .content("내용")
                .sameGenderStatus(SameGender.NO)
                .vehicle(Vehicle.밴)
                .numberOfParticipants(3)
                .fare(1000)
                .duration(100)
                .user(users)
                .build();
        postsRepository.save(posts);
        when(postsRepository.save(any())).thenReturn(posts); // postsRepository.save() 호출 시 Mock 객체의 반환 설정
        // assertThat(postsRepository.findAll()).hasSize(1);

        ParticipationDetails participationDetails = ParticipationDetails.builder()
                .user(users)
                .post(posts).build();

        // When
        participationDetailsRepository.save(participationDetails);

        // Then
        List<PostsMemberDto> postsMembers = postsService.findPostsMembers(1L); // 해당 게시물의 멤버 리스트 조회
        // assertThat(postsMembers).isNotNull(); // 조회된 멤버 리스트가 null이 아님을 검증
        // assertThat(postsMembers).hasSize(1);
    }
}