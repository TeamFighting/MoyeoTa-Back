package com.moyeota.moyeotaproject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moyeota.moyeotaproject.controller.dto.postsdto.PostsGetResponseDto;
import com.moyeota.moyeotaproject.controller.dto.postsdto.PostsMemberDto;
import com.moyeota.moyeotaproject.controller.dto.postsdto.PostsSaveRequestDto;
import com.moyeota.moyeotaproject.controller.dto.postsdto.PostsUpdateRequestDto;
import com.moyeota.moyeotaproject.domain.chatroom.ChatRoom;
import com.moyeota.moyeotaproject.domain.chatroom.ChatRoomRepository;
import com.moyeota.moyeotaproject.domain.participationDetails.ParticipationDetails;
import com.moyeota.moyeotaproject.domain.participationDetails.ParticipationDetailsRepository;
import com.moyeota.moyeotaproject.domain.posts.Category;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.posts.PostsRepository;
import com.moyeota.moyeotaproject.domain.posts.PostsStatus;
import com.moyeota.moyeotaproject.domain.totalDetail.TotalDetail;
import com.moyeota.moyeotaproject.domain.totalDetail.TotalDetailRepository;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class PostsService {

	private final UsersService usersService;
	private final ChatRoomRepository chatRoomRepository;
	private final ParticipationDetailsService participationDetailsService;
	private final UsersRepository usersRepository;
	private final PostsRepository postsRepository;
	private final ParticipationDetailsRepository participationDetailsRepository;
	private final TotalDetailRepository totalDetailRepository;

	@Transactional(readOnly = true)
	public List<PostsMemberDto> findPostsMembers(Long postId) {
		List<ParticipationDetails> participationDetailsList =
			participationDetailsRepository.findParticipationDetailsByPostsId(postId);
		List<PostsMemberDto> postsMemberDtoList = new ArrayList<>();
		for (int i = 0; i < participationDetailsList.size(); i++) {
			postsMemberDtoList.add(PostsMemberDto.builder().user(participationDetailsList.get(i).getUser()).build());
		}
		return postsMemberDtoList;
	}

	@Transactional(readOnly = true)
	public List<PostsGetResponseDto> findAllDesc() {
		List<Posts> postsList = postsRepository.findAllByStatus(PostsStatus.RECRUITING);
		List<PostsGetResponseDto> responseDtoList = new ArrayList<>();
		for (int i = 0; i < postsList.size(); i++) {
			Posts post = postsList.get(i);
			PostsGetResponseDto postsResponseDto = new PostsGetResponseDto(post, post.getUser());
			responseDtoList.add(postsResponseDto);
		}
		return responseDtoList;
	}

	@Transactional(readOnly = true)
	public PostsGetResponseDto findById(Long postId) {
		updateView(postId);
		Posts posts = postsRepository.findById(postId).orElseThrow(()
			-> new IllegalArgumentException("해당 게시글이 없습니다. id=" + postId));

		PostsGetResponseDto responseDto = PostsGetResponseDto.builder()
			.posts(posts)
			.users(posts.getUser())
			.build();
		return responseDto;
	}

	public Long save(String accessToken, PostsSaveRequestDto requestDto, Long roomId) {
		Users user = usersService.getUserByToken(accessToken);
		Optional<ChatRoom> chatRoom = chatRoomRepository.findById(roomId);
		if (chatRoom.isEmpty()) {
			throw new IllegalArgumentException("해당 채팅방이 없습니다. id=" + roomId);
		}
		Posts post = requestDto.toEntity(user, chatRoom.get());
		Long postId = postsRepository.save(post).getId();
		participationDetailsService.join(user.getId(), postId);
		return postId;
	}

	public Long update(String accessToken, Long postId, PostsUpdateRequestDto requestDto) {
		usersService.getUserByToken(accessToken);
		Posts posts = postsRepository.findById(postId).orElseThrow(()
			-> new IllegalArgumentException("해당 모집글이 없습니다. id=" + postId));

		posts.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getCategory(),
			requestDto.getDeparture(), requestDto.getDestination(), requestDto.getDepartureTime(),
			requestDto.getSameGenderStatus(), requestDto.getVehicle(), requestDto.getNumberOfRecruitment(),
			requestDto.getFare(), requestDto.getDuration(), requestDto.getDistance());
		return postId;
	}

	public void delete(String accessToken, Long postId) {
		usersService.getUserByToken(accessToken);
		Posts posts = postsRepository.findById(postId).orElseThrow(()
			-> new IllegalArgumentException("해당 모집글이 없습니다. id=" + postId));
		postsRepository.delete(posts);
	}

	public void complete(Long postId) {
		Posts post = postsRepository.findById(postId).orElseThrow(()
			-> new IllegalArgumentException("해당 모집글이 없습니다. id=" + postId));
		post.postsComplete();
	}

	public Slice<PostsGetResponseDto> findByIdDesc(Long userId, Pageable pageable) {
		Users user = usersRepository.findById(userId).orElseThrow(()
			-> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));
		Slice<Posts> postsSlice = postsRepository.findByUser(user, pageable);
		Slice<PostsGetResponseDto> postsResponseDtoList = postsSlice.map(
			p -> new PostsGetResponseDto(p, p.getUser()));
		return postsResponseDtoList;
	}

	public void cancelParticipation(Long postId) {
		Posts post = postsRepository.findById(postId).orElseThrow(()
			-> new IllegalArgumentException("해당 모집글이 없습니다. id=" + postId));
		post.minusUser();
	}

	public Slice<PostsGetResponseDto> findAllByCategory(Category category, Pageable pageable) {
		Slice<Posts> postsSlice = postsRepository.findByCategory(category, PostsStatus.RECRUITING, pageable);
		Slice<PostsGetResponseDto> postsResponseDtoList = postsSlice.map(
			p -> new PostsGetResponseDto(p, p.getUser()));
		return postsResponseDtoList;
	}

	@Transactional
	public int updateView(Long id) {
		return postsRepository.updateView(id);
	}

	public Long calcPrice(Long postId) {
		List<ParticipationDetails> list = participationDetailsRepository.findParticipationDetailsByPostsId(postId);
		TotalDetail totalDetail = totalDetailRepository.findByPostId(postId).orElseThrow(()
			-> new IllegalArgumentException("해당 내역이 없습니다. id=" + postId));

		double sum = 0;
		double totalPayment = totalDetail.getTotalPayment();
		for (int i = 0; i < list.size(); i++) {
			sum += list.get(i).getDistance();
		}

		for (int i = 0; i < list.size(); i++) {
			ParticipationDetails participationDetails = list.get(i);
			participationDetails.updatePrice((participationDetails.getDistance() / sum) * totalPayment);
		}
		return postId;
	}
}
