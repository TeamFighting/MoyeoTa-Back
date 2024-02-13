package com.moyeota.moyeotaproject.domain.users;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class UsersRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;

    private Users users;

    @BeforeEach
    public void setUp() {
        users = Users.builder()
                .loginId("tae77777")
                .password("rlaxogjs8312")
                .email("tae77777@naver.com")
                .build();
    }

    @DisplayName("유저 저장 테스트")
    @Test
    @Order(1)
    public void userSave() {
        System.out.println(">>> SetUp Users: " + users.toString());

        // given
        users = Users.builder()
                .name("김태헌")
                .loginId("tae77777")
                .password("rlaxogjs8312")
                .email("tae77777@naver.com")
                .build();

        System.out.println(">>> Origin Users: " + users.toString());

        // when
        Users savedUsers = usersRepository.save(users);
        System.out.println(">>> Saved Users: " + savedUsers);

        // then
        assertNotNull(savedUsers.getId());
        assertEquals(savedUsers.getName(), "김태헌");
        assertEquals(savedUsers.getLoginId(), "tae77777");
        assertTrue(savedUsers.getId() > 0);
    }

    @Test
    void existsByLoginId() {
    }

    @Test
    void findByEmail() {
    }

    @Test
    void findNameByUserId() {
    }
}