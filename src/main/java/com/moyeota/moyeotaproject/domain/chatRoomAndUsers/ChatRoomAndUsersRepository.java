package com.moyeota.moyeotaproject.domain.chatRoomAndUsers;

import com.moyeota.moyeotaproject.domain.chatRoom.ChatRoom;
import com.moyeota.moyeotaproject.domain.users.Users;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomAndUsersRepository extends JpaRepository<ChatRoomAndUsers, Long> {

    ChatRoomAndUsers findByChatRoomAndUser(ChatRoom chatRoom, Users user);

    List<ChatRoomAndUsers> findAllByUserOrderByCreatedDateDesc(Users user);
}