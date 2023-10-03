package com.moyeota.moyeotaproject.controller.dto.reviewDto;

import com.moyeota.moyeotaproject.domain.review.Review;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponseDto {

    @ApiModelProperty(value = "별점")
    private double starRate;

    @ApiModelProperty(value = "리뷰 내용")
    private String content;

    @ApiModelProperty(value = "작성 시각")
    private LocalDateTime createdAt;

    @Builder
    public ReviewResponseDto(Review review) {
        this.starRate = review.getStarRate();
        this.content = review.getContent();
        this.createdAt = review.getCreatedDate();
    }

}
