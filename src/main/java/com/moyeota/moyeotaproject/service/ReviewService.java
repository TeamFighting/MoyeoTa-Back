package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.controller.dto.reviewDto.ReviewResponseDto;
import com.moyeota.moyeotaproject.controller.dto.reviewDto.ReviewSaveRequestDto;
import com.moyeota.moyeotaproject.domain.review.Review;
import com.moyeota.moyeotaproject.domain.review.ReviewRepository;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UsersRepository usersRepository;

    public Long save(Long userId, ReviewSaveRequestDto requestDto) {
        Users user = usersRepository.findById(userId).orElseThrow(()
        -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));
        return reviewRepository.save(requestDto.toEntity(user)).getId();
    }

    public void delete(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(()
        -> new IllegalArgumentException("해당 리뷰가 없습니다. id=" + reviewId));
        reviewRepository.delete(review);
    }

    @Transactional(readOnly = true)
    public Slice<ReviewResponseDto> findAllDesc(Long userId, Pageable pageable) {
        Users user = usersRepository.findById(userId).orElseThrow(()
                -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));
        Slice<Review> reviewSlice = reviewRepository.findByUser(user, pageable);
        Slice<ReviewResponseDto> reviewResponseDtos = reviewSlice.map(r -> new ReviewResponseDto(r));
        return reviewResponseDtos;
    }

}
