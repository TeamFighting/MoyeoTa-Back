package com.moyeota.moyeotaproject.controller.dto;

import com.moyeota.moyeotaproject.domain.review.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponseDto {

    private double starRate;
    private String content;
    private LocalDateTime createdAt;

    @Builder
    public ReviewResponseDto(Review review) {
        this.starRate = review.getStarRate();
        this.content = review.getContent();
        this.createdAt = review.getCreatedDate();
    }

}
