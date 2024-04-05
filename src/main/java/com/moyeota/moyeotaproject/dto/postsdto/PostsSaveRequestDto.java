package com.moyeota.moyeotaproject.dto.postsdto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import com.moyeota.moyeotaproject.domain.BaseTimeEntity;
import com.moyeota.moyeotaproject.domain.chatroom.ChatRoom;
import com.moyeota.moyeotaproject.domain.posts.Category;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.posts.SameGender;
import com.moyeota.moyeotaproject.domain.posts.Vehicle;
import com.moyeota.moyeotaproject.domain.users.Users;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "모집글 작성 요청")
public class PostsSaveRequestDto extends BaseTimeEntity {

	@ApiModelProperty(value = "모집글 제목", example = "공릉역에서 어의관 갈사람?", required = true)
	@NotBlank(message = "제목을 입력하세요")
	private String title;

	@ApiModelProperty(value = "카테고리", example = "LIFE", required = true)
	private Category category;

	@ApiModelProperty(value = "출발지", example = "공릉역 7호선", required = true)
	@NotBlank(message = "출발지를 입력하세요.")
	private String departure;

	@ApiModelProperty(value = "목적지", example = "서울과학기술대학교 어의관", required = true)
	@NotBlank(message = "목적지를 입력하세요.")
	private String destination;

	@ApiModelProperty(value = "출발 시각", example = "2023-09-04 19:57:13.000000", required = true)
	private LocalDateTime departureTime;

	@ApiModelProperty(value = "내용", example = "같이 갈 사람 참가 신청 ㄱㄱ", required = true)
	private String content;

	@ApiModelProperty(value = "이성 가능여부", example = "YES", required = true)
	private SameGender sameGenderStatus;

	@ApiModelProperty(value = "이동 수단", example = "일반", required = true)
	private Vehicle vehicle;

	@ApiModelProperty(value = "모집 인원", example = "4", required = true)
	private int numberOfRecruitment;

	@ApiModelProperty(value = "택시 요금", example = "5200")
	private int fare;

	@ApiModelProperty(value = "이동 시간(초)", example = "313")
	private int duration;

	@ApiModelProperty(value = "이동 거리(km)", example = "0.5")
	private double distance;

	@ApiModelProperty(value = "채팅방 id", example = "uuid")
	@NotBlank(message = "채팅방 id를 입력하세요.")
	private String roomId;

	public Posts toEntity(Users user, ChatRoom chatRoom) {
		return Posts.builder()
			.title(title)
			.category(category)
			.departure(departure)
			.destination(destination)
			.departureTime(departureTime)
			.content(content)
			.sameGenderStatus(sameGenderStatus)
			.vehicle(vehicle)
			.numberOfRecruitment(numberOfRecruitment)
			.numberOfParticipants(0)
			.fare(fare)
			.duration(duration)
			.distance(distance)
			.user(user)
			.chatRoom(chatRoom)
			.build();
	}
}
