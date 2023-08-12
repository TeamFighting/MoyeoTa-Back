package com.moyeota.moyeotaproject.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.posts.SameGender;
import com.moyeota.moyeotaproject.domain.users.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {

    private String title;
    private String departure;
    private String destination;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime departureTime;
    private String content;
    private SameGender sameGenderStatus;
    private int numberOfRecruitment;
    private int numberOfParticipants;

    @Builder
    public PostsSaveRequestDto(String title, String departure, String destination, LocalDateTime departureTime, String content, SameGender sameGenderStatus, int numberOfRecruitment, int numberOfParticipants) {
        this.title = title;
        this.departure = departure;
        this.destination = destination;
        this.departureTime = departureTime;
        this.content = content;
        this.sameGenderStatus = sameGenderStatus;
        this.numberOfRecruitment = numberOfRecruitment;
        this.numberOfParticipants = numberOfParticipants;
    }

    public Posts toEntity(Users user) {
        return Posts.builder()
                .title(title)
                .departure(departure)
                .destination(destination)
                .departureTime(departureTime)
                .content(content)
                .sameGenderStatus(sameGenderStatus)
                .numberOfRecruitment(numberOfRecruitment)
                .numberOfParticipants(numberOfParticipants)
                .user(user)
                .build();
    }
}
