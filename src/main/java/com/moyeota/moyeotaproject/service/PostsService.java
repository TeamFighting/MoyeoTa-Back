package com.moyeota.moyeotaproject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moyeota.moyeotaproject.domain.account.Account;
import com.moyeota.moyeotaproject.domain.account.AccountRepository;
import com.moyeota.moyeotaproject.domain.chatroom.ChatRoom;
import com.moyeota.moyeotaproject.domain.chatroom.ChatRoomRepository;
import com.moyeota.moyeotaproject.domain.location.Location;
import com.moyeota.moyeotaproject.domain.location.LocationRepository;
import com.moyeota.moyeotaproject.domain.participationdetails.ParticipationDetails;
import com.moyeota.moyeotaproject.domain.participationdetails.ParticipationDetailsRepository;
import com.moyeota.moyeotaproject.domain.posts.Category;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.posts.PostsRepository;
import com.moyeota.moyeotaproject.domain.posts.PostsStatus;
import com.moyeota.moyeotaproject.domain.totaldetail.TotalDetail;
import com.moyeota.moyeotaproject.domain.totaldetail.TotalDetailRepository;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;
import com.moyeota.moyeotaproject.dto.accountdto.AccountDto;
import com.moyeota.moyeotaproject.dto.postsdto.MembersLocationResponseDto;
import com.moyeota.moyeotaproject.dto.postsdto.PostsGetResponseDto;
import com.moyeota.moyeotaproject.dto.postsdto.PostsMemberDto;
import com.moyeota.moyeotaproject.dto.postsdto.PostsSaveRequestDto;
import com.moyeota.moyeotaproject.dto.postsdto.PostsUpdateRequestDto;

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
	private final AccountRepository accountRepository;
	private final LocationRepository locationRepository;

	@Transactional(readOnly = true)
	public List<PostsMemberDto> findPostsMembers(Long postId) {
		List<ParticipationDetails> participationDetailsList =
			participationDetailsRepository.findParticipationDetailsByPostsId(postId);

		List<PostsMemberDto> postsMemberDtoList = new ArrayList<>();
		for (int i = 0; i < participationDetailsList.size(); i++) {
			Users user = participationDetailsList.get(i).getUser();
			boolean isPotOwner = false;
			List<Account> accounts = accountRepository.findAllByUser(user);
			List<AccountDto> accountDtos = new ArrayList<>();
			for (int j = 0; j < accounts.size(); j++) {
				Account account = accounts.get(j);
				AccountDto accountDto = AccountDto.builder()
					.bankName(account.getBankName())
					.accountNumber(account.getAccountNumber())
					.build();
				accountDtos.add(accountDto);
			}
			if (participationDetailsList.get(i).getPost().getUser().getId() == user.getId()) {
				isPotOwner = true;
			}
			//닉네임 생성여부 체크 후, 닉네임이 없으면 실명으로 표시
			if (participationDetailsList.get(i).getUser().getNickName() == null ||
				participationDetailsList.get(i).getUser().getNickName().equals("")) {
				postsMemberDtoList
					.add(PostsMemberDto.builder()
						.user(user)
						.nickname(user.getName())
						.isPotOwner(isPotOwner)
						.payment(participationDetailsList.get(i).isPayment())
						.accounts(accountDtos)
						.build());
			} else {
				postsMemberDtoList
					.add(PostsMemberDto.builder()
						.user(user)
						.nickname(user.getNickName())
						.isPotOwner(isPotOwner)
						.payment(participationDetailsList.get(i).isPayment())
						.accounts(accountDtos)
						.build());
			}
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
		participationDetailsService.join(accessToken, postId);
		return postId;
	}

	public Long update(String accessToken, Long postId, PostsUpdateRequestDto requestDto) {
		usersService.getUserByToken(accessToken);
		Posts posts = postsRepository.findById(postId).orElseThrow(()
			-> new IllegalArgumentException("해당 모집글이 없습니다. id=" + postId));

		posts.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getCategory(),
			requestDto.getDeparture(), requestDto.getDestination(), requestDto.getDepartureTime(),
			requestDto.getSameGenderStatus(), requestDto.getVehicle(), requestDto.getNumberOfRecruitment(),
			requestDto.getFare(), requestDto.getDuration(), requestDto.getDistance(), requestDto.getLatitude(),
			requestDto.getLongitude());
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

	public List<MembersLocationResponseDto> findMembersLocation(Long postId) {
		Posts post = postsRepository.findById(postId).orElseThrow(()
			-> new IllegalArgumentException("해당 모집글이 없습니다. id=" + postId));

		List<ParticipationDetails> participationDetailsList = participationDetailsRepository.findParticipationDetailsByPostsId(
			postId);

		List<MembersLocationResponseDto> membersLocationList = new ArrayList<>();
		for (int i = 0; i < participationDetailsList.size(); i++) {
			Long userId = participationDetailsList.get(i).getUser().getId();
			boolean isOwner = false;
			if (userId == post.getUser().getId()) {
				isOwner = true;
			}
			Optional<Location> location = locationRepository.findTopByUserIdAndPostIdOrderByIdDesc(
				String.valueOf(userId), String.valueOf(postId));
			MembersLocationResponseDto membersLocationResponseDto = MembersLocationResponseDto.builder()
				.isOwner(isOwner)
				.position(location.get().getPosition())
				.userId(userId)
				.build();
			membersLocationList.add(membersLocationResponseDto);
		}

		return membersLocationList;
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
		double basicFare = 4500;
		for (int i = 0; i < list.size(); i++) {
			sum += list.get(i).getPrice() - basicFare;
		}

		for (int i = 0; i < list.size(); i++) {
			ParticipationDetails participationDetails = list.get(i);
			participationDetails.updatePrice(
				basicFare / list.size() + (totalPayment - basicFare) * ((list.get(i).getPrice() - basicFare) / sum));
		}
		return postId;
	}

}
