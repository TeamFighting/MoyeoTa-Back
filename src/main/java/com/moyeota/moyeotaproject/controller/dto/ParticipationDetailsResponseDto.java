package com.moyeota.moyeotaproject.controller.dto;

import com.moyeota.moyeotaproject.domain.participationDetails.ParticipationDetailsStatus;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ParticipationDetailsResponseDto {

    private String title;
    private String departure;
    private String destination;
    private LocalDateTime departureTime;
    private ParticipationDetailsStatus status;

    @Builder
    public ParticipationDetailsResponseDto(Posts posts, ParticipationDetailsStatus status) {
        this.title = posts.getTitle();
        this.departure = posts.getDeparture();
        this.destination = posts.getDestination();
        this.departureTime = posts.getDepartureTime();
        this.status = status;
    }
}
