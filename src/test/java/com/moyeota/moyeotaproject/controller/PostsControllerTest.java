package com.moyeota.moyeotaproject.controller;

import com.moyeota.moyeotaproject.config.jwtConfig.JwtAuthenticationFilter;
import com.moyeota.moyeotaproject.service.ParticipationDetailsService;
import com.moyeota.moyeotaproject.service.PostsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostsController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class PostsControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    PostsService postsService;

    @MockBean
    ParticipationDetailsService participationDetailsService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("게시글 저장 컨트롤러 테스트")
    void testSave() throws Exception {
        //given

        //when

        //then
        mvc.perform(post("/api/posts/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\" :  \"테스트 제목\", \"category\" :  \"테스트 카테고리\"}"))
                .andExpect(status().isForbidden());
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

    @Test
    @DisplayName("게시글 내림차순으로 모두 조회")
    void findAllDesc() throws Exception {
        String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMCIsImV4cCI6MTcwNTkxMDkxMX0.xK5NqP0s9E1Jj2zfaI9MgR7KLEyXXnnrSana0x4tyXA";
        mvc.perform(get("/api/posts/")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().is2xxSuccessful());
    }

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