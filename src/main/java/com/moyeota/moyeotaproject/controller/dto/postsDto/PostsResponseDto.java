package com.moyeota.moyeotaproject.controller.dto.postsDto;

import com.moyeota.moyeotaproject.domain.posts.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@ApiModel(value = "모집글 조회 응답")
public class PostsResponseDto {

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
    @ApiModelProperty(value = "생성 시각")
    private LocalDateTime createAt;
    @ApiModelProperty(value = "모집글 상태")
    private PostsStatus status;
    @ApiModelProperty(value = "유저 이름")
    private String userName;
    @ApiModelProperty(value = "프로필 이미지")
    private String profileImage;
    @ApiModelProperty(value = "유저 성별")
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
