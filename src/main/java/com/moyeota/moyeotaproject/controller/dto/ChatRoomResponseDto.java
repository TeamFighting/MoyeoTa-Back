package com.moyeota.moyeotaproject.controller.dto;

import com.moyeota.moyeotaproject.domain.chatMessage.ChatMessage;
import com.moyeota.moyeotaproject.domain.chatMessage.ChatMessageDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatRoomResponseDto {

    private String name;
    private LocalDateTime createdDate;
    private String lastMessage;

    @Builder
    public ChatRoomResponseDto(String name, LocalDateTime createdDate, ChatMessage message) {
        this.name = name;
        this.createdDate = createdDate;
        this.lastMessage = message.getMessage();
    }
}
