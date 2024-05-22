package com.moyeota.moyeotaproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeota.moyeotaproject.domain.chatroom.ChatRoom;
import com.moyeota.moyeotaproject.domain.posts.*;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;
import com.moyeota.moyeotaproject.dto.UsersDto.TokenInfoDto;
import com.moyeota.moyeotaproject.service.PostsService;
import com.moyeota.moyeotaproject.service.SchoolService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
public class PostsControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostsService postsService;

    @MockBean
    private UsersRepository usersRepository;

    @MockBean
    private PostsRepository postsRepository;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        Long userId = 1L;

        Users userEntity = Users.builder()
                .id(userId)
                .name("테스트사용자")
                .email("tae77777@naver.com")
                .age("24")
                .loginId("moyeota")
                .password("moyeota")
                .build();
        when(usersRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        ChatRoom chatRoomEntity = ChatRoom.builder()
                .roomId("1")
                .name("채팅방이름")
                .build();

        Posts postsEntity = Posts.builder()
                .user(userEntity)
                .title("title")
                .category(Category.LIFE)
                .departure("출발지")
                .destination("도착지")
                .departureTime(LocalDateTime.MIN)
                .content("내용")
                .sameGenderStatus(SameGender.NO)
                .vehicle(Vehicle.일반)
                .numberOfRecruitment(4)
                .numberOfParticipants(2)
                .fare(10000)
                .duration(1000)
                .distance(1000)
                .latitude("24")
                .longitude("25")
                .chatRoom(chatRoomEntity)
                .build();
        when(postsRepository.findById(1L)).thenReturn(Optional.of(postsEntity));
    }

    @Test
    void 모집글_작성() {

    }

    @Test
    void 모집글_수정() {

    }

    @Test
    void 모집글_삭제() {

    }

    @Test
    void 모집글_마감() {

    }

    @Test
    void 특정_모집글_조회() throws Exception {
        mockMvc.perform(get("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void 모집글_전체_조회() throws Exception {
        mockMvc.perform(get("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 모집글_전체_조회시_토큰이_없는_경우도_현재_가능() throws Exception {
        mockMvc.perform(get("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                // .andExpect(status().isUnauthorized());
                .andExpect(status().isOk());
    }

    @Test
    void 사용자별_모집글_전체_조회() throws Exception {
        mockMvc.perform(get("/api/posts/users/1?page=0")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 카테고리별_모집글_전체_조회() throws Exception {
        mockMvc.perform(get("/api/posts/search?category=LIFE&page=0")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 모집글_파티원_목록_조회() throws Exception {
        mockMvc.perform(get("/api/posts/1/members?postId=1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 택시_이용_종료시_더치페이_금액_계산() throws Exception {
        mockMvc.perform(post("/api/posts/calculation/1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 파티원_내린_지점() throws Exception {
        mockMvc.perform(get("/api/posts/1/members/location")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }
}