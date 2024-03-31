package com.moyeota.moyeotaproject.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moyeota.moyeotaproject.dto.participationdetailsdto.DistancePriceDto;
import com.moyeota.moyeotaproject.dto.participationdetailsdto.ParticipationDetailsResponseDto;
import com.moyeota.moyeotaproject.dto.postsdto.PostsGetResponseDto;
import com.moyeota.moyeotaproject.domain.participationdetails.ParticipationDetails;
import com.moyeota.moyeotaproject.domain.participationdetails.ParticipationDetailsRepository;
import com.moyeota.moyeotaproject.domain.participationdetails.ParticipationDetailsStatus;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.posts.PostsRepository;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class ParticipationDetailsService {

	private final UsersService usersService;
	private final ChatRoomService chatRoomService;
	private final UsersRepository usersRepository;
	private final PostsRepository postsRepository;
	private final ParticipationDetailsRepository participationDetailsRepository;

	public Long join(String accessToken, Long postId) {
		Users user = usersService.getUserByToken(accessToken);
		Posts post = postsRepository.findById(postId).orElseThrow(()
			-> new IllegalArgumentException("해당 모집글이 없습니다. id=" + postId));
		post.addUser();
		chatRoomService.addUser(post.getChatRoom().getId(), user.getId());

		if (post.getNumberOfParticipants() == post.getNumberOfRecruitment()) {
			post.postsComplete();
		}

		ParticipationDetails participationDetails = ParticipationDetails.builder()
			.user(user)
			.post(post)
			.build();
		return participationDetailsRepository.save(participationDetails).getId();
	}

	@Transactional(readOnly = true)
	public ParticipationDetails findById(Long participationDetailsId) {
		ParticipationDetails participationDetails = participationDetailsRepository.findById(participationDetailsId)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + participationDetailsId));
		return participationDetails;
	}

	public ParticipationDetails checkParticipation(String accessToken, Long postId) {
		Users user = usersService.getUserByToken(accessToken);
		if (!participationDetailsRepository.findParticipationDetailsByUserAndPost(user,
			postsRepository.findById(postId).get()).isPresent()) {
			return null;
		}
		return participationDetailsRepository.findParticipationDetailsByUserAndPost(user,
			postsRepository.findById(postId).get()).get();
	}

	@Transactional(readOnly = true)
	public List<ParticipationDetailsResponseDto> findAllDesc(String accessToken) {
		Users user = usersService.getUserByToken(accessToken);
		List<ParticipationDetails> participationList = participationDetailsRepository.findByUserOrderByIdDesc(user);
		List<ParticipationDetailsResponseDto> responseDtoList = new ArrayList<>();
		for (int i = 0; i < participationList.size(); i++) {
			if (participationList.get(i).getStatus().equals(ParticipationDetailsStatus.JOIN)) {
				ParticipationDetailsResponseDto responseDto = ParticipationDetailsResponseDto.builder()
					.posts(participationList.get(i).getPost())
					.status(participationList.get(i).getStatus())
					.build();
				responseDtoList.add(responseDto);
			}
		}

		Collections.sort(responseDtoList, new DateComparator());
		return responseDtoList;
	}

	public List<PostsGetResponseDto> findMyParticipationDetailsDesc(String accessToken) {
		Users user = usersService.getUserByToken(accessToken);
		List<ParticipationDetails> participationList = participationDetailsRepository.findByUserOrderByIdDesc(user);
		List<PostsGetResponseDto> list = new ArrayList<>();
		for (int i = 0; i < participationList.size(); i++) {
			Posts post = participationList.get(i).getPost();
			if (post.getDepartureTime().isAfter(LocalDateTime.now())) {
				PostsGetResponseDto responseDto = PostsGetResponseDto.builder()
					.posts(post)
					.users(post.getUser())
					.build();
				list.add(responseDto);
			}
		}
		return list;

	}

	public boolean cancelParticipation(Long postId, String accessToken) {
		Users user = usersService.getUserByToken(accessToken);
		Posts post = postsRepository.findById(postId).orElseThrow(()
			-> new IllegalArgumentException("해당 모집글이 없습니다. id=" + postId));
		ParticipationDetails participationDetails =
			participationDetailsRepository.findParticipationDetailsByUserAndPost(user, post)
				.orElseThrow(() -> new IllegalArgumentException("해당 참가내역이 없습니다."));

		chatRoomService.deleteUser(post.getChatRoom().getId(), user.getId());
		participationDetailsRepository.delete(participationDetails);
		return true;
	}

	public Long saveDistance(String accessToken, Long postId, double distance) {
		Users user = usersService.getUserByToken(accessToken);
		Posts post = postsRepository.findById(postId).orElseThrow(
			() -> new IllegalArgumentException("해당 모집글이 없습니다. id=" + postId));
		ParticipationDetails participationDetails =
			participationDetailsRepository.findParticipationDetailsByUserAndPost(user, post)
				.orElseThrow(() -> new IllegalArgumentException("해당 참가내역이 없습니다."));

		participationDetails.updateDistance(distance);
		return participationDetails.getId();
	}

	public DistancePriceDto findDistanceAndPrice(Long userId, Long postId) {
		Users user = usersRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));
		Posts post = postsRepository.findById(postId).orElseThrow(
			() -> new IllegalArgumentException("해당 모집글이 없습니다. id=" + postId));
		ParticipationDetails participationDetails =
			participationDetailsRepository.findParticipationDetailsByUserAndPost(user, post)
				.orElseThrow(() -> new IllegalArgumentException("해당 참가내역이 없습니다."));

		DistancePriceDto distancePriceDto = DistancePriceDto.builder()
			.distance(participationDetails.getDistance())
			.price(participationDetails.getPrice())
			.build();
		return distancePriceDto;
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
