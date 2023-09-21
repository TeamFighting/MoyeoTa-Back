package com.moyeota.moyeotaproject.domain.posts;

import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;
    @Autowired
    UsersRepository usersRepository;

    @Test
    public void 모집글저장_불러오기1() {
        String title = "test posts";
        Category category = Category.LIFE;
        String departure = "공릉역";
        String destination = "서울과학기술대학교 어의관";
        LocalDateTime departureTime = LocalDateTime.now();
        String content = "과기대 학생을 찾습니다.";
        SameGender sameGender = SameGender.YES;
        Vehicle vehicle = Vehicle.일반;
        int numberOfRecruitment = 3;

        Users user = Users.builder()
                .name("kyko")
                .profileImage("profileImage")
                .phoneNumber("010-1111-2222")
                .email("koc@naver.com")
                .loginId("loginId")
                .password("password")
                .status("status")
                .gender(true)
                .averageStarRate(3.5F)
                .school("seoultech")
                .isAuthenticated(true)
                .build();
        usersRepository.save(user);

        Posts post = Posts.builder()
                .title(title)
                .category(category)
                .departure(departure)
                .destination(destination)
                .departureTime(departureTime)
                .content(content)
                .sameGenderStatus(sameGender)
                .numberOfRecruitment(numberOfRecruitment)
                .user(user)
                .vehicle(vehicle)
                .build();

        postsRepository.save(post);

        List<Posts> postsList = postsRepository.findAllDesc();

        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getCategory()).isEqualTo(category);
        assertThat(posts.getDeparture()).isEqualTo(departure);
        assertThat(posts.getDestination()).isEqualTo(destination);
        assertThat(posts.getContent()).isEqualTo(content);
        assertThat(posts.getVehicle()).isEqualTo(vehicle);
        assertThat(posts.getNumberOfRecruitment()).isEqualTo(numberOfRecruitment);
        assertThat(posts.getSameGenderStatus()).isEqualTo(sameGender);
        assertThat(posts.getUser()).isEqualTo(user);

    }

}