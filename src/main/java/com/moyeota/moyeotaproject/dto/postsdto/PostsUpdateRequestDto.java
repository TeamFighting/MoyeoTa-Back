package com.moyeota.moyeotaproject.dto.postsdto;

import java.time.LocalDateTime;

import com.moyeota.moyeotaproject.domain.posts.Category;
import com.moyeota.moyeotaproject.domain.posts.SameGender;
import com.moyeota.moyeotaproject.domain.posts.Vehicle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
	@ApiModelProperty(value = "이동 거리(km)")
	private double distance;
	@ApiModelProperty(value = "위도")
	private String latitude;
	@ApiModelProperty(value = "경도")
	private String longitude;

}
