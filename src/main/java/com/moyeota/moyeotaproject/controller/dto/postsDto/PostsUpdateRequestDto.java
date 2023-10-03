package com.moyeota.moyeotaproject.controller.dto.postsDto;

import com.moyeota.moyeotaproject.domain.posts.Category;
import com.moyeota.moyeotaproject.domain.posts.SameGender;
import com.moyeota.moyeotaproject.domain.posts.Vehicle;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "모집글 수정 요청")
public class PostsUpdateRequestDto {

    @ApiModelProperty(value = "모집글 제목")
    private String title;

    @ApiModelProperty(value = "내용")
    private String content;

    @ApiModelProperty(value = "카테고리")
    private Category category;

    @ApiModelProperty(value = "출발지")
    private String departure;

    @ApiModelProperty(value = "목적지")
    private String destination;

    @ApiModelProperty(value = "출발 시각", example = "2023-09-04 19:57:13.000000")
    private LocalDateTime departureTime;

    @ApiModelProperty(value = "동성만 가능 여부", example = "YES가 동성만")
    private SameGender sameGenderStatus;

    @ApiModelProperty(value = "이동 수단")
    private Vehicle vehicle;

    @ApiModelProperty(value = "모집 인원")
    private int numberOfRecruitment;

    @ApiModelProperty(value = "택시 요금")
    private int fare;

    @ApiModelProperty(value = "이동 시간(초)")
    private int duration;

    @Builder
    public PostsUpdateRequestDto(String title, String content, Category category, String departure, String destination, LocalDateTime departureTime, SameGender sameGenderStatus, Vehicle vehicle, int numberOfRecruitment, int fare, int duration) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.departure = departure;
        this.destination = destination;
        this.departureTime = departureTime;
        this.sameGenderStatus = sameGenderStatus;
        this.vehicle = vehicle;
        this.numberOfRecruitment = numberOfRecruitment;
        this.fare = fare;
        this.duration = duration;
    }

}