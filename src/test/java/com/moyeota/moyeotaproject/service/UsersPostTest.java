package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.domain.chatRoom.ChatRoom;
import com.moyeota.moyeotaproject.domain.posts.*;
import com.moyeota.moyeotaproject.domain.users.Users;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


public class UsersPostTest {

    private Users user;
    private Posts post;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ChatRoom chatRoom = ChatRoom.builder().name("testChatRoom")
                .roomId("asdfghjkl").userCount(0).build();
        user = Users.builder()
                .name("김태헌")
                .email("tae77777@naver.com")
                .loginId("jakeheon")
                .password("1234")
                .build();
        post = Posts.builder()
                .title("Test Post")
                .category(Category.COLLEGE)
                .departure("A")
                .destination("B")
                .departureTime(LocalDateTime.now())
                .content("This is a test post.")
                .sameGenderStatus(SameGender.YES)
                .vehicle(Vehicle.밴)
                .numberOfRecruitment(5)
                .numberOfParticipants(0)
                .fare(10000)
                .duration(60)
                .user(user)
                .chatRoom(chatRoom)
                .build();
    }

    @DisplayName("게시글에 사용자 추가")
    @Test
    public void testAddUser() {
        // 사용자 추가
        post.addUser();
        // 참여자 수 확인
        Assert.assertEquals(1, post.getNumberOfParticipants());
    }


    @DisplayName("게시글에 사용자 제거")
    @Test
    public void testMinusUser() {
        // 사용자 추가
        post.addUser();

        // 사용자 제거
        post.minusUser();

        // 참여자 수 확인
        assertEquals(0, post.getNumberOfParticipants());
        assertEquals(PostsStatus.RECRUITING, post.getStatus());
    }

    @DisplayName("게시글 업데이트")
    @Test
    public void testPostUpdate() {
        post.update("Updated 제목", "Updated 내용", Category.COLLEGE, "X", "Y", LocalDateTime.now(), SameGender.NO, Vehicle.일반, 3, 5000, 30, 18);
        assertEquals("Updated 제목", post.getTitle());
        assertEquals("Updated 내용", post.getContent());
        assertEquals(Category.COLLEGE, post.getCategory());
        assertEquals("X", post.getDeparture());
        assertEquals("Y", post.getDestination());
        assertEquals(SameGender.NO, post.getSameGenderStatus());
        assertEquals(Vehicle.일반, post.getVehicle());
        assertEquals(3, post.getNumberOfRecruitment());
        assertEquals(5000, post.getFare());
        assertEquals(30, post.getDuration());
        assertEquals(18, post.getDistance());
    }

    @DisplayName("게시글 상태 완료")
    @Test
    public void testPostsComplete() {
        post.postsComplete();
        assertEquals(PostsStatus.COMPLETE, post.getStatus());
    }

}

