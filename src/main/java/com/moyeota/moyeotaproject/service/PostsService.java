package com.moyeota.moyeotaproject.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeota.moyeotaproject.controller.dto.KakaoApiDto.DurationAndFareResponseDto;
import com.moyeota.moyeotaproject.controller.dto.PostsResponseDto;
import com.moyeota.moyeotaproject.controller.dto.PostsSaveRequestDto;
import com.moyeota.moyeotaproject.controller.dto.PostsUpdateRequestDto;
import com.moyeota.moyeotaproject.domain.posts.Category;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.posts.PostsRepository;
import com.moyeota.moyeotaproject.domain.posts.PostsStatus;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class PostsService {

    private final UsersRepository usersRepository;
    private final PostsRepository postsRepository;

    private final RestTemplate restTemplate;
    private static final String kakaoUrl = "https://apis-navi.kakaomobility.com/v1/directions";
    private String apiKey = "638ae9079b9543346a397132235ea935";

    @Transactional(readOnly = true)
    public List<PostsResponseDto> findAllDesc() {
        List<Posts> postsList = postsRepository.findAllDesc();
        List<PostsResponseDto> list = new ArrayList<>();
        for (int i=0; i<postsList.size(); i++){
            if(postsList.get(i).getStatus() == PostsStatus.RECRUITING) {
                PostsResponseDto responseDto = PostsResponseDto.builder()
                        .posts(postsList.get(i))
                        .userName(postsList.get(i).getUser().getName())
                        .profileImage(postsList.get(i).getUser().getProfileImage())
                        .userGender(postsList.get(i).getUser().getGender()).build();
                list.add(responseDto);
            }
        }
        return list;
    }

    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long postId) {
        Posts posts = postsRepository.findById(postId).orElseThrow(()
        -> new IllegalArgumentException("해당 게시글이 없습니다. id="+ postId));

        PostsResponseDto responseDto = PostsResponseDto.builder()
                .posts(posts)
                .userName(posts.getUser().getName())
                .profileImage(posts.getUser().getProfileImage())
                .userGender(posts.getUser().getGender()).build();

        return responseDto;
    }

    public Long save(Long userId, PostsSaveRequestDto requestDto){
//        Users users1 = Users.builder()
//                .name("kyko").profileImage("profile")
//                .phoneNumber("010-1111-1111")
//                .email("kyko@naver.com")
//                .loginId("loginId")
//                .password("password")
//                .status("join")
//                .gender(true)
//                .school("seoultech")
//                .averageStarRate(3.5F)
//                .isAuthenticated(true)
//                .provider(OAuthProvider.KAKAO)
//                .build();
//        usersRepository.save(users1); //제거예정
//        Users users2 = Users.builder()
//                .name("kyko22").profileImage("profile")
//                .phoneNumber("010-1111-1111")
//                .email("kyko@naver.com")
//                .loginId("loginId")
//                .password("password")
//                .status("join")
//                .gender(true)
//                .school("seoultech")
//                .averageStarRate(3.5F)
//                .isAuthenticated(true)
//                .provider(OAuthProvider.KAKAO)
//                .build();
//        usersRepository.save(users2);

        Users user = usersRepository.findById(userId).orElseThrow(()
        -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));

        Posts post = requestDto.toEntity(user);
        return postsRepository.save(post).getId();
    }

    public Long update(Long postId, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(postId).orElseThrow(()
        -> new IllegalArgumentException("해당 모집글이 없습니다. id=" + postId));

        posts.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getCategory(), requestDto.getDeparture(), requestDto.getDestination(), requestDto.getDepartureTime(), requestDto.getSameGenderStatus(), requestDto.getVehicle(), requestDto.getNumberOfRecruitment(), requestDto.getFare(), requestDto.getDuration());
        return postId;
    }

    public void delete(Long postId) {
        Posts posts = postsRepository.findById(postId).orElseThrow(()
        -> new IllegalArgumentException("해당 모집글이 없습니다. id=" + postId));
        postsRepository.delete(posts);
    }

    public void completePost(Long postId) {
        Posts post = postsRepository.findById(postId).orElseThrow(()
        -> new IllegalArgumentException("해당 모집글이 없습니다. id=" + postId));
        post.postsComplete();
    }

    public List<PostsResponseDto> findMyPostsByIdDesc(Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow(()
        -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));
        List<Posts> postsList = postsRepository.findByUserOrderByCreatedDateDesc(user);
        List<PostsResponseDto> list = new ArrayList<>();
        for (int i=0; i<postsList.size(); i++){
            PostsResponseDto responseDto = PostsResponseDto.builder()
                    .posts(postsList.get(i))
                    .userName(postsList.get(i).getUser().getName())
                    .profileImage(postsList.get(i).getUser().getProfileImage())
                    .userGender(postsList.get(i).getUser().getGender()).build();
            list.add(responseDto);
        }
        return list;
    }

    public void cancelParticipation(Long postId) {
        Posts post = postsRepository.findById(postId).orElseThrow(()
        -> new IllegalArgumentException("해당 모집글이 없습니다. id=" + postId));
        post.minusUser();
    }

    public List<PostsResponseDto> findAllByCategoryDesc(Category category) {
        List<Posts> postsList = postsRepository.findByCategoryOrderByIdDesc(category);
        List<PostsResponseDto> list = new ArrayList<>();
        if(postsList.size() == 0)
            return list;
        for (int i=0; i<postsList.size(); i++){
            if(postsList.get(i).getStatus() == PostsStatus.RECRUITING) {
                PostsResponseDto responseDto = PostsResponseDto.builder()
                        .posts(postsList.get(i))
                        .userName(postsList.get(i).getUser().getName())
                        .profileImage(postsList.get(i).getUser().getProfileImage())
                        .userGender(postsList.get(i).getUser().getGender()).build();
                list.add(responseDto);
            }
        }
        return list;
    }

    public DurationAndFareResponseDto getDurationAndFare(String origin, String destination) throws ParseException {
        if(origin == null || origin.equals(""))
            throw new IllegalArgumentException("출발지 정보가 없습니다.");
        if(destination == null || destination.equals(""))
            throw new IllegalArgumentException("목적지 정보가 없습니다.");

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(kakaoUrl);
        uriComponentsBuilder.queryParam("origin", origin);
        uriComponentsBuilder.queryParam("destination", destination);
        URI uri = uriComponentsBuilder.build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);
        headers.set("Content-Type", "application/json");
        HttpEntity httpEntity = new HttpEntity<>(headers);

        String result =  restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class).getBody();

        JSONParser parser = new JSONParser();

        JSONObject object = (JSONObject) parser.parse(result);
        JSONArray routes = (JSONArray) object.get("routes");
        int taxi = 0;
        int duration = 0;
        for (int i=0; i<routes.size(); i++){
            object = (JSONObject) routes.get(i);
            JSONObject summary = (JSONObject) object.get("summary");
            JSONObject fare = (JSONObject) summary.get("fare");
            taxi = Integer.parseInt(fare.get("taxi").toString());
            JSONArray sections = (JSONArray) object.get("sections");
            for (int j=0; j<sections.size(); j++){
                object = (JSONObject) sections.get(j);
                duration = Integer.parseInt(object.get("duration").toString());
            }
        }

        DurationAndFareResponseDto durationAndFareResponseDto = new DurationAndFareResponseDto(taxi, duration);
        log.info("durationAndFareResponseDto: " + durationAndFareResponseDto);
        if (Objects.isNull(durationAndFareResponseDto)) {
            throw new IllegalArgumentException("해당 경로에 대한 정보가 없습니다. 출발지=" + origin + ", 목적지=" + destination);
        }
        return durationAndFareResponseDto;
    }
}
