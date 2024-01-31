package com.moyeota.moyeotaproject.controller.dto.chatDto;

import com.moyeota.moyeotaproject.domain.chatMessage.ChatMessage;
import com.moyeota.moyeotaproject.domain.chatMessage.ChatMessageDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatRoomResponseDto {

    private String name;
    private String roomId;
    private int userCount;
    private LocalDateTime createdDate;
    private String lastMessage;

    @Builder
    public ChatRoomResponseDto(String name, String roomId, int userCount, LocalDateTime createdDate, ChatMessage message) {
        this.name = name;
        this.roomId = roomId;
        this.userCount = userCount;
        this.createdDate = createdDate;
        this.lastMessage = message.getMessage();
    }
}
