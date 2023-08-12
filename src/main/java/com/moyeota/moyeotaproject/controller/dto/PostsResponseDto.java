package com.moyeota.moyeotaproject.controller.dto;

import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.posts.PostsStatus;
import com.moyeota.moyeotaproject.domain.posts.SameGender;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostsResponseDto {

    private String title;
    private String departure;
    private String destination;
    private LocalDateTime departureTime;
    private String content;
    private SameGender sameGenderStatus;
    private int numberOfRecruitment;
    private int numberOfParticipants;
    private PostsStatus status;
    private String userName;
    private String profileImage;
    private Float userAverageStarRate;
    private boolean userGender;

    @Builder
    public PostsResponseDto(Posts posts, String userName, String profileImage, Float userAverageStarRate, boolean userGender) {
        this.title = posts.getTitle();
        this.departure = posts.getDeparture();
        this.destination = posts.getDestination();
        this.departureTime = posts.getDepartureTime();
        this.content = posts.getContent();
        this.sameGenderStatus = posts.getSameGenderStatus();
        this.numberOfRecruitment = posts.getNumberOfRecruitment();
        this.numberOfParticipants = posts.getNumberOfParticipants();
        this.status = posts.getStatus();
        this.userName = userName;
        this.profileImage = profileImage;
        this.userAverageStarRate = userAverageStarRate;
        this.userGender = userGender;
    }

}
