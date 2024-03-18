package com.moyeota.moyeotaproject.domain.chatroomandusers;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.moyeota.moyeotaproject.domain.BaseTimeEntity;
import com.moyeota.moyeotaproject.domain.chatroom.ChatRoom;
import com.moyeota.moyeotaproject.domain.users.Users;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoomAndUsers extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "chatRoomId")
	private ChatRoom chatRoom;

	@ManyToOne
	@JoinColumn(name = "usersId")
	private Users user;

	public void setChatRoom(ChatRoom chatRoom) {
		this.chatRoom = chatRoom;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	@Builder
	public ChatRoomAndUsers(ChatRoom chatRoom, Users user) {
		setChatRoom(chatRoom);
		setUser(user);
	}
}
