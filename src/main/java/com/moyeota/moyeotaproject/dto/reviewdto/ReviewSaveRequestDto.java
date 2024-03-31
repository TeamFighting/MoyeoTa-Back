package com.moyeota.moyeotaproject.dto.reviewdto;

import com.moyeota.moyeotaproject.domain.review.Review;
import com.moyeota.moyeotaproject.domain.users.Users;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(value = "리뷰 작성 요청")
public class ReviewSaveRequestDto {

	@ApiModelProperty(value = "별점", example = "3.5", required = true)
	private double starRate;

	@ApiModelProperty(value = "리뷰 내용", example = "친절하십니다.", required = true)
	private String content;

	public Review toEntity(Users user) {
		return Review.builder()
			.starRate(starRate)
			.content(content)
			.user(user)
			.build();
	}

}
