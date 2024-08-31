package com.moyeota.moyeotaproject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moyeota.moyeotaproject.dto.chatroomdto.ChatRoomResponseDto;
import com.moyeota.moyeotaproject.domain.chatroom.ChatRoom;
import com.moyeota.moyeotaproject.domain.chatroom.ChatRoomRepository;
import com.moyeota.moyeotaproject.domain.chatroomandusers.ChatRoomAndUsers;
import com.moyeota.moyeotaproject.domain.chatroomandusers.ChatRoomAndUsersRepository;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.posts.PostsRepository;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;
	private final UsersRepository usersRepository;
	private final ChatRoomAndUsersRepository chatRoomAndUsersRepository;
	private final PostsRepository postsRepository;
	private final UsersService usersService;

	public Long createRoom(String roomName, String roomId) {
		ChatRoom chatRoom = ChatRoom.builder()
			.roomId(roomId)
			.name(roomName)
			.userCount(0)
			.build();
		return chatRoomRepository.save(chatRoom).getId();
	}

	public List<ChatRoomResponseDto> findAllRoomsByUserDesc(String accessToken) {
		Users user = usersService.getUserByToken(accessToken);
		List<ChatRoomAndUsers> chatRoomList = chatRoomAndUsersRepository.findAllByUserOrderByModifiedDateDesc(user);
		List<ChatRoomResponseDto> chatRoomResponseDtoList = new ArrayList<>();
		for (int i = 0; i < chatRoomList.size(); i++) {
			ChatRoom chatRoom = chatRoomList.get(i).getChatRoom();
			List<ChatRoomAndUsers> chatRoomAndUsersList = chatRoomAndUsersRepository.findAllByChatRoom(chatRoom);
			List<String> profileImageList = getProfileImages(chatRoomAndUsersList);
			ChatRoomResponseDto chatRoomResponseDto = ChatRoomResponseDto.builder()
				.chatRoom(chatRoom)
				.posts(chatRoom.getPost())
				.profileImageList(profileImageList)
				.build();
			chatRoomResponseDtoList.add(chatRoomResponseDto);
		}
		return chatRoomResponseDtoList;
	}

	public void deleteRoom(String accessToken, Long postId) {
		Users user = usersService.getUserByToken(accessToken);
		Posts post = postsRepository.findById(postId).orElseThrow(
			() -> new IllegalArgumentException("해당 모집글이 없습니다. id=" + postId)
		);
		deleteUser(post.getChatRoom().getId(), user.getId());
	}

	//채팅방 유저 추가
	public Long addUser(Long chatRoomId, Long userId) {
		Users user = usersRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(
			() -> new IllegalArgumentException("해당 채팅방이 없습니다. id=" + chatRoomId));

		increaseUser(chatRoomId);
		ChatRoomAndUsers chatRoomAndUsers = ChatRoomAndUsers.builder().user(user).chatRoom(chatRoom).build();
		return chatRoomAndUsersRepository.save(chatRoomAndUsers).getId();
	}

	//채팅방 유저 퇴장
	public void deleteUser(long chatRoomId, long userId) {
		Users user = usersRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(
			() -> new IllegalArgumentException("해당 채팅방이 없습니다. id=" + chatRoomId));
		ChatRoomAndUsers chatRoomAndUsers = chatRoomAndUsersRepository.findByChatRoomAndUser(chatRoom, user).orElseThrow(
			() -> new IllegalArgumentException("참가 내역이 없습니다."));

		decreaseUser(chatRoomId);
		chatRoomAndUsersRepository.delete(chatRoomAndUsers);
	}

	//채팅방 인원 + 1
	public void increaseUser(long chatRoomId) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(
			() -> new IllegalArgumentException("해당 채팅방이 없습니다. id= " + chatRoomId));
		chatRoom.setUserCount(chatRoom.getUserCount() + 1);
	}

	//채팅방 인원 - 1
	public void decreaseUser(long chatRoomId) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(
			() -> new IllegalArgumentException("해당 채팅방이 없습니다. id= " + chatRoomId));
		chatRoom.setUserCount(chatRoom.getUserCount() - 1);
	}

	public List<String> getProfileImages(List<ChatRoomAndUsers> chatRoomAndUsersList) {
		List<String> profileImageList = new ArrayList<>();
		for (int i = 0; i < chatRoomAndUsersList.size(); i++) {
			profileImageList.add(chatRoomAndUsersList.get(i).getUser().getProfileImage());
		}
		return profileImageList;
	}

}
