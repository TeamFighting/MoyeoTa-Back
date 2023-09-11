package com.moyeota.moyeotaproject.domain.chatMessage;

import com.moyeota.moyeotaproject.domain.chatRoom.ChatRoom;
import com.moyeota.moyeotaproject.domain.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByChatRoomOrderByIdDesc(ChatRoom chatRoom);

    List<MessageRoomIdMapping> findRoomIdByUser(Users user);

    @Query("select c from ChatMessage c where c.roomId = :roomId order by c.createdDate desc")
    List<ChatMessage> findChatMessage(@Param("roomId") String roomId);

}
