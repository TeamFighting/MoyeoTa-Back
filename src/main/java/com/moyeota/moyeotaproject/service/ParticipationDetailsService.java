package com.moyeota.moyeotaproject.service;


import com.moyeota.moyeotaproject.controller.dto.ParticipationDetailsResponseDto;
import com.moyeota.moyeotaproject.domain.participationDetails.ParticipationDetails;
import com.moyeota.moyeotaproject.domain.participationDetails.ParticipationDetailsRepository;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.posts.PostsRepository;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Transactional
@Service
public class ParticipationDetailsService {

    private final UsersRepository usersRepository;
    private final PostsRepository postsRepository;
    private final ParticipationDetailsRepository participationDetailsRepository;

    public Long join(Long userId, Long postId) {
        Users user = usersRepository.findById(userId).orElseThrow(()
                -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));

        Posts post = postsRepository.findById(postId).orElseThrow(()
        -> new IllegalArgumentException("해당 모집글이 없습니다. id=" + postId));

        post.addUser();
        if(post.getNumberOfParticipants() == post.getNumberOfRecruitment())
            post.postsComplete();

        ParticipationDetails participationDetails = ParticipationDetails.builder()
                .user(user)
                .post(post)
                .build();

        return participationDetailsRepository.save(participationDetails).getId();
    }

    @Transactional(readOnly = true)
    public ParticipationDetails findById(Long participationDetailsId) {
        ParticipationDetails participationDetails = participationDetailsRepository.findById(participationDetailsId).orElseThrow(()
        -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + participationDetailsId));

        return participationDetails;
    }

    public ParticipationDetails checkParticipation(Long userId, Long postId) {
        return participationDetailsRepository.findByUserAndPost(usersRepository.findById(userId).get(), postsRepository.findById(postId).get());
    }

    @Transactional(readOnly = true)
    public List<ParticipationDetailsResponseDto> findAllDesc(Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow(()
        -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));
        List<ParticipationDetails> participationDetailsList = participationDetailsRepository.findByUserOrderByIdDesc(user);
        List<ParticipationDetailsResponseDto> responseDtoList = new ArrayList<>();
        for (int i=0; i<participationDetailsList.size(); i++) {
            ParticipationDetailsResponseDto responseDto = ParticipationDetailsResponseDto.builder()
                    .posts(participationDetailsList.get(i).getPost())
                    .status(participationDetailsList.get(i).getStatus())
                    .build();
            responseDtoList.add(responseDto);
        }

        Collections.sort(responseDtoList, new DateComparator());
        return responseDtoList;
    }

    public boolean cancelParticipation(Long participationDetailsId) {
        ParticipationDetails participationDetails = participationDetailsRepository.findById(participationDetailsId).orElseThrow(()
                -> new IllegalArgumentException("해당 참가내역이 없습니다. id=" + participationDetailsId));
        return participationDetails.cancel();
    }

    //모집글의 출발 날짜 비교 로직
    static class DateComparator implements Comparator<ParticipationDetailsResponseDto> {
        @Override
        public int compare(ParticipationDetailsResponseDto p1, ParticipationDetailsResponseDto p2) {
            int result = p2.getDepartureTime().compareTo(p1.getDepartureTime());
            return result;
        }
    }

}
