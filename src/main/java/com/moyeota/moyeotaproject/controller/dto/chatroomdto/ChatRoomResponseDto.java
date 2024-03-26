package com.moyeota.moyeotaproject.controller.dto.chatroomdto;

import java.time.LocalDateTime;
import java.util.List;

import com.moyeota.moyeotaproject.domain.chatroom.ChatRoom;
import com.moyeota.moyeotaproject.domain.posts.Posts;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatRoomResponseDto {

	private Long postId;
	private String roomName;
	private String roomId;
	private int userCount;
	private LocalDateTime createdDate;
	private List<String> profileImageList;

	@Builder
	public ChatRoomResponseDto(ChatRoom chatRoom, Posts posts, List<String> profileImageList) {
		this.postId = posts.getId();
		this.roomName = chatRoom.getName();
		this.roomId = chatRoom.getRoomId();
		this.userCount = chatRoom.getUserCount();
		this.createdDate = chatRoom.getCreatedDate();
		this.profileImageList = profileImageList;
	}
}
