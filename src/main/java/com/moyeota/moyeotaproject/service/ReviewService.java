package com.moyeota.moyeotaproject.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moyeota.moyeotaproject.dto.reviewdto.ReviewResponseDto;
import com.moyeota.moyeotaproject.dto.reviewdto.ReviewSaveRequestDto;
import com.moyeota.moyeotaproject.domain.review.Review;
import com.moyeota.moyeotaproject.domain.review.ReviewRepository;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class ReviewService {

	private final UsersService usersService;
	private final ReviewRepository reviewRepository;
	private final UsersRepository usersRepository;

	public Long save(String accessToken, Long userId, ReviewSaveRequestDto requestDto) {
		usersService.getUserByToken(accessToken);
		Users reviewedUser = usersRepository.findById(userId).orElseThrow(()
			-> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));
		return reviewRepository.save(requestDto.toEntity(reviewedUser)).getId();
	}

	public void delete(String accessToken, Long reviewId) {
		usersService.getUserByToken(accessToken);
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
