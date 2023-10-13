package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.config.jwtConfig.JwtTokenProvider;
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
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UsersRepository usersRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public Long save(String accessToken, Long userId, ReviewSaveRequestDto requestDto) {
        getUserByToken(accessToken);
        Users user = usersRepository.findById(userId).orElseThrow(()
        -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));
        return reviewRepository.save(requestDto.toEntity(user)).getId();
    }

    public void delete(String accessToken, Long reviewId) {
        getUserByToken(accessToken);
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

    public Users getUserByToken(String accessToken) {
        Optional<Users> users = usersRepository.findById(jwtTokenProvider.extractSubjectFromJwt(accessToken));
        if (users.isPresent()) {
            return users.get();
        } else {
            throw new RuntimeException("토큰에 해당하는 멤버가 없습니다.");
        }
    }

}
