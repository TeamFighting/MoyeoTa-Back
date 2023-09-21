package com.moyeota.moyeotaproject.controller.dto;

import com.moyeota.moyeotaproject.domain.posts.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostsResponseDto {

    private String title;
    private Category category;
    private String departure;
    private String destination;
    private LocalDateTime departureTime;
    private String content;
    private SameGender sameGenderStatus;
    private Vehicle vehicle;
    private int numberOfRecruitment;
    private int numberOfParticipants;
    private int fare;
    private int duration;
    private LocalDateTime createAt;
    private PostsStatus status;
    private String userName;
    private String profileImage;
    private boolean userGender;

    @Builder
    public PostsResponseDto(Posts posts, String userName, String profileImage, boolean userGender) {
        this.title = posts.getTitle();
        this.category = posts.getCategory();
        this.departure = posts.getDeparture();
        this.destination = posts.getDestination();
        this.departureTime = posts.getDepartureTime();
        this.content = posts.getContent();
        this.sameGenderStatus = posts.getSameGenderStatus();
        this.vehicle = posts.getVehicle();
        this.numberOfRecruitment = posts.getNumberOfRecruitment();
        this.numberOfParticipants = posts.getNumberOfParticipants();
        this.fare = posts.getFare();
        this.duration = posts.getDuration();
        this.createAt = posts.getCreatedDate();
        this.status = posts.getStatus();
        this.userName = userName;
        this.profileImage = profileImage;
        this.userGender = userGender;
    }

}
