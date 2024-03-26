package com.moyeota.moyeotaproject.domain.chatroomandusers;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moyeota.moyeotaproject.domain.chatroom.ChatRoom;
import com.moyeota.moyeotaproject.domain.users.Users;

public interface ChatRoomAndUsersRepository extends JpaRepository<ChatRoomAndUsers, Long> {

	Optional<ChatRoomAndUsers> findByChatRoomAndUser(ChatRoom chatRoom, Users user);

	List<ChatRoomAndUsers> findAllByUserOrderByModifiedDateDesc(Users user);

	List<ChatRoomAndUsers> findAllByChatRoom(ChatRoom chatRoom);

}
