package com.moyeota.moyeotaproject.controller.dto.reviewDto;

import com.moyeota.moyeotaproject.domain.review.Review;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@ApiModel(value = "특정 유저 리뷰 전체 조회 응답")
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
