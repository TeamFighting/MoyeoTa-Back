package com.moyeota.moyeotaproject.domain.chatMessage;

import com.moyeota.moyeotaproject.domain.chatRoom.ChatRoom;
import com.moyeota.moyeotaproject.domain.users.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageDto {

    private String message;
    private String roomId;
    private String sender;

    private MessageType type;

    private Long userId;
    private Long chatRoomId;

    @Builder
    public ChatMessageDto(String message, String roomId, String sender, MessageType type, Long userId, Long chatRoomId) {
        this.message = message;
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.userId = userId;
        this.chatRoomId = chatRoomId;
    }

    public ChatMessage toEntity(Users user, ChatRoom chatRoom) {
        return ChatMessage.builder()
                .message(message)
                .type(type)
                .user(user)
                .chatRoom(chatRoom)
                .build();

    }

}
