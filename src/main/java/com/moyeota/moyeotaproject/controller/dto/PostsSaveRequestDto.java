package com.moyeota.moyeotaproject.controller.dto;

import com.moyeota.moyeotaproject.domain.posts.Category;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.posts.SameGender;
import com.moyeota.moyeotaproject.domain.posts.Vehicle;
import com.moyeota.moyeotaproject.domain.users.Entity.Users;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "모집글 작성 요청")
public class PostsSaveRequestDto {

    @ApiModelProperty(value = "모집글 제목", example = "서울에서 제주까지 가즈아~~", required = true)
    private String title;

    @ApiModelProperty(value = "카테고리", example = "일상", required = true)
    private Category category;

    @ApiModelProperty(value = "출발지", example = "서울", required = true)
    private String departure;

    @ApiModelProperty(value = "목적지", example = "제주", required = true)
    private String destination;

    @ApiModelProperty(value = "출발 시각", example = "2023-09-04 19:57:13.000000", required = true)
    private LocalDateTime departureTime;

    @ApiModelProperty(value = "내용", example = "제주도에 같이 갈 사람은 참가 신청 ㄱㄱ", required = true)
    private String content;

    @ApiModelProperty(value = "이성 가능여부", example = "YES", required = true)
    private SameGender sameGenderStatus;

    @ApiModelProperty(value = "이동 수단", example = "일반", required = true)
    private Vehicle vehicle;

    @ApiModelProperty(value = "모집 인원", example = "4", required = true)
    private int numberOfRecruitment;


    @Builder
    public PostsSaveRequestDto(String title, String departure, String destination, LocalDateTime departureTime, String content, SameGender sameGenderStatus, int numberOfRecruitment) {
        this.title = title;
        this.departure = departure;
        this.destination = destination;
        this.departureTime = departureTime;
        this.content = content;
        this.sameGenderStatus = sameGenderStatus;
        this.numberOfRecruitment = numberOfRecruitment;
    }

    public Posts toEntity(Users user) {
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
                .numberOfParticipants(1)
                .user(user)
                .build();
    }
}
