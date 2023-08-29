package com.moyeota.moyeotaproject.controller.dto;

import com.moyeota.moyeotaproject.domain.review.Review;
import com.moyeota.moyeotaproject.domain.users.Entity.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewSaveRequestDto {

    private double starRate;
    private String content;

    @Builder
    public ReviewSaveRequestDto(double starRate, String content) {
        this.starRate = starRate;
        this.content = content;
    }

    public Review toEntity(Users user) {
        return Review.builder()
                .starRate(starRate)
                .content(content)
                .user(user)
                .build();
    }

}
