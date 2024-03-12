package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.config.jwtConfig.JwtTokenProvider;
import com.moyeota.moyeotaproject.controller.dto.totalDetailDto.TotalDetailRequestDto;
import com.moyeota.moyeotaproject.controller.dto.totalDetailDto.TotalDetailResponseDto;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.posts.PostsRepository;
import com.moyeota.moyeotaproject.domain.totalDetail.TotalDetail;
import com.moyeota.moyeotaproject.domain.totalDetail.TotalDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class TotalDetailService {

    private final JwtTokenProvider jwtTokenProvider;
    private final PostsService postsService;
    private final PostsRepository postsRepository;
    private final TotalDetailRepository totalDetailRepository;

    public Long save(String accessToken, TotalDetailRequestDto requestDto, Long postId) {
        // postsService.getUserByToken(accessToken);
        Posts post = postsRepository.findById(postId).orElseThrow(()
        -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + postId));
        TotalDetail totalDetail = requestDto.toEntity(post);
        Long totalDetailId = totalDetailRepository.save(totalDetail).getId();
        return totalDetailId;
    }

    public TotalDetailResponseDto findTotalDetailById(Long totalDetailId) {
        TotalDetail totalDetail = totalDetailRepository.findById(totalDetailId).orElseThrow(()
                -> new IllegalArgumentException("해당 정보가 없습니다. id=" + totalDetailId));

        TotalDetailResponseDto responseDto = TotalDetailResponseDto.builder()
                .totalDistance(totalDetail.getTotalDistance())
                .totalPayment(totalDetail.getTotalPayment())
                .build();
        return responseDto;
    }

}
