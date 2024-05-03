//package com.moyeota.moyeotaproject.domain.users;
//
//import com.moyeota.moyeotaproject.config.jwtconfig.JwtTokenGenerator;
//import com.moyeota.moyeotaproject.config.jwtconfig.JwtTokenProvider;
//import com.moyeota.moyeotaproject.dto.UsersDto.TokenInfoDto;
//import com.moyeota.moyeotaproject.service.UsersService;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.TransactionStatus;
//import org.springframework.transaction.support.DefaultTransactionDefinition;
//
//import java.util.Date;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@Slf4j
//@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
//@AutoConfigureMockMvc
//@SpringBootTest
//class UsersRepositoryTest {
//
//    @Autowired
//    PlatformTransactionManager transactionManager;
//
//    TransactionStatus status;
//
//    String salt = "RlaXoVBsYt9V7zq57TejMnVUyzblYcfPQye08f7MGVA9XkHa";
//
//    @Autowired
//    private UsersRepository usersRepository;
//
//    @Autowired
//    private UsersService usersService;
//
//    public JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(salt);
//    public JwtTokenGenerator jwtTokenGenerator = new JwtTokenGenerator(jwtTokenProvider);
//
//    private String accessToken;
//
//    @BeforeEach
//    void beforeEach() {
//        // 트랜잭션 시작
//        status = transactionManager.getTransaction(new DefaultTransactionDefinition());
//        Long userId = 100L;
//        jwtTokenProvider.generateToken(userId.toString(),
//                new Date(1000 * 60 * 60 * 24 * 21));
//        TokenInfoDto generate = jwtTokenGenerator.generate(1L);
//        System.out.println(generate);
//        System.out.println(generate.getAccessToken());
//        accessToken = generate.getAccessToken();
//    }
//
////    @AfterEach
////    void afterEach() {
////        // 트랜잭션 롤백
////        transactionManager.rollback(status);
////    }
//
//    @Test
//    @Order(1)
//    public void 유저_저장_테스트() {
//        // given
//        Users users = Users.builder()
//                .name("테스트사용자")
//                .loginId("moyeota")
//                .password("moyeota")
//                .email("tae77777@naver.com")
//                .build();
//
//        // when
//        Users savedUsers = usersRepository.save(users);
//        System.out.println(">>> Saved Users: " + savedUsers);
//
//        // then
//        assertThat(savedUsers.getId()).isEqualTo(1L);
//        assertThat(savedUsers.getName()).isEqualTo("테스트사용자");
//        assertThat(savedUsers.getLoginId()).isEqualTo("moyeota");
//        assertThat(savedUsers.getEmail()).isEqualTo("tae77777@naver.com");
//    }
//
//}