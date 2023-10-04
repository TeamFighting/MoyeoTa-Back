package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.domain.users.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OAuthLoginServiceTest {

    @Autowired
    private OAuthLoginService oAuthLoginService;

    @Test
    public void 소셜로그인테스트() {

    }

}