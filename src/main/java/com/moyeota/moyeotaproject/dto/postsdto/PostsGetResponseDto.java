package com.moyeota.moyeotaproject.dto.postsdto;

import java.time.LocalDateTime;

import com.moyeota.moyeotaproject.domain.posts.Category;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.posts.PostsStatus;
import com.moyeota.moyeotaproject.domain.posts.SameGender;
import com.moyeota.moyeotaproject.domain.posts.Vehicle;
import com.moyeota.moyeotaproject.domain.users.Users;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@ApiModel(value = "모집글 조회 응답")
public class PostsGetResponseDto {

	@ApiModelProperty(value = "모집글 인덱스")
	private Long postId;
	@ApiModelProperty(value = "모집글 제목")
	private String title;
	@ApiModelProperty(value = "카테고리")
	private Category category;
	@ApiModelProperty(value = "출발지")
	private String departure;
	@ApiModelProperty(value = "도착지")
	private String destination;
	@ApiModelProperty(value = "출발 시각")
	private LocalDateTime departureTime;
	@ApiModelProperty(value = "내용")
	private String content;
	@ApiModelProperty(value = "동성만 가능 여부", example = "YES가 동성만")
	private SameGender sameGenderStatus;
	@ApiModelProperty(value = "운송 수단")
	private Vehicle vehicle;
	@ApiModelProperty(value = "총 모집원")
	private int numberOfRecruitment;
	@ApiModelProperty(value = "현재 참가자")
	private int numberOfParticipants;
	@ApiModelProperty(value = "요금")
	private int fare;
	@ApiModelProperty(value = "이동시간(s)")
	private int duration;
	@ApiModelProperty(value = "이동거리(km)")
	private double distance;
	@ApiModelProperty(value = "조회수")
	private int view;
	@ApiModelProperty(value = "생성 시각")
	private LocalDateTime createAt;
	@ApiModelProperty(value = "모집글 상태")
	private PostsStatus status;
	@ApiModelProperty(value = "유저 이름")
	private String userName;
	@ApiModelProperty(value = "프로필 이미지")
	private String profileImage;
	@ApiModelProperty(value = "유저 성별")
	private String userGender;
	@ApiModelProperty(value = "채팅방 인덱스")
	private String roomId;

	@Builder
	public PostsGetResponseDto(Posts posts, Users users) {
		this.postId = posts.getId();
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
		this.distance = posts.getDistance();
		this.view = posts.getView();
		this.createAt = posts.getCreatedDate();
		this.status = posts.getStatus();
		this.roomId = posts.getChatRoom().getRoomId();
		this.userName = users.getName();
		this.profileImage = users.getProfileImage();
		this.userGender = users.getGender();
	}

}
