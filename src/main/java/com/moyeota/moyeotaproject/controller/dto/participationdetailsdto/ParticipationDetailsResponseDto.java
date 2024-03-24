package com.moyeota.moyeotaproject.controller.dto.participationdetailsdto;

import java.time.LocalDateTime;

import com.moyeota.moyeotaproject.domain.participationdetails.ParticipationDetailsStatus;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.posts.Vehicle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@ApiModel(value = "참가내역 조회 응답")
public class ParticipationDetailsResponseDto {

	@ApiModelProperty(value = "출발지")
	private String departure;
	@ApiModelProperty(value = "도착지")
	private String destination;
	@ApiModelProperty(value = "출발시각")
	private LocalDateTime departureTime;
	@ApiModelProperty(value = "이동수단")
	private Vehicle vehicle;
	@ApiModelProperty(value = "금액")
	private int fare;
	@ApiModelProperty(value = "참가여부")
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
