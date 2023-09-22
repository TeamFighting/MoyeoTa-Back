package com.moyeota.moyeotaproject.controller.dto;

import com.moyeota.moyeotaproject.domain.participationDetails.ParticipationDetailsStatus;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.posts.Vehicle;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ParticipationDetailsResponseDto {

    private String departure;
    private String destination;
    private LocalDateTime departureTime;
    private Vehicle vehicle;
    private int fare;
    private ParticipationDetailsStatus status;

    @Builder
    public ParticipationDetailsResponseDto(Posts posts, ParticipationDetailsStatus status) {
        this.departure = posts.getDeparture();
        this.destination = posts.getDestination();
        this.departureTime = posts.getDepartureTime();
        this.vehicle = posts.getVehicle();
        this.fare = posts.getFare();
        this.status = status;
    }
}
