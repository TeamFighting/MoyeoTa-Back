package com.moyeota.moyeotaproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeota.moyeotaproject.config.jwtConfig.JwtAuthenticationFilter;
import com.moyeota.moyeotaproject.controller.dto.UsersDto;
import com.moyeota.moyeotaproject.controller.dto.postsDto.PostsSaveRequestDto;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.posts.PostsRepository;
import com.moyeota.moyeotaproject.domain.posts.SameGender;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;
import com.moyeota.moyeotaproject.service.ParticipationDetailsService;
import com.moyeota.moyeotaproject.service.PostsService;
import com.moyeota.moyeotaproject.service.UsersService;
import org.joda.time.DateTimeUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@AutoConfigureMockMvc
public class PostsControllerTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PostsService postsService;

    @Autowired
    PostsRepository postsRepository;

    @Autowired
    UsersService usersService;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    MockMvc mvc;

    private static final String BASE_URL = "/api/posts";
    private static final String testAccessToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiZXhwIjoxNzEwMzE4MjIwfQ.ZOuV7sb4nH2QjWVyH8zyzqMdneb7yLn64slbiWcIofA";

    @Test
    void 게시글_저장_컨트롤러_테스트() throws Exception {
        //given
        Users users = Users.builder()
                .loginId("loginId")
                .password("password")
                .email("email")
                .build();
        usersRepository.save(users);
        String stringDate = "2023-09-04 19:57:13.000000";
        DateTimeFormatter stringDateFormatted = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        LocalDateTime dateTime = LocalDateTime.parse(stringDate, stringDateFormatted);

        PostsSaveRequestDto postsSaveRequestDto = PostsSaveRequestDto.builder()
                .title("테스트 제목")
                .departure("테스트 출발 장소")
                .destination("테스트 도착 장소")
                .departureTime(dateTime)
                .content("테스트 내용")
                .sameGenderStatus(SameGender.NO)
                .numberOfRecruitment(4)
                .fare(15000)
                .duration(1800)
                .build();
        Posts posts = postsSaveRequestDto.toEntity(users);
        postsRepository.save(posts);
        //when
        postsService.save(testAccessToken, postsSaveRequestDto);
        String body = objectMapper.writeValueAsString(postsSaveRequestDto);
        /* Object를 JSON으로 변환 */

        //then
        mvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", testAccessToken)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data").value(1));
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void completePost() {
    }

    @Test
    void findById() {
    }

//    @Test
//    @DisplayName("게시글 내림차순으로 모두 조회")
//    void findAllDesc() throws Exception {
//        String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMCIsImV4cCI6MTcwNTkxMDkxMX0.xK5NqP0s9E1Jj2zfaI9MgR7KLEyXXnnrSana0x4tyXA";
//        mvc.perform(get("/api/posts/")
//                        .header("Authorization", "Bearer " + jwtToken))
//                .andExpect(status().is2xxSuccessful());
//    }

    @Test
    void findMyPostsByIdDesc() {
    }

    @Test
    void findAllByCategoryDesc() {
    }

    @Test
    void findPostsMembers() {
    }

    @Test
    void calcPrice() {
    }
}