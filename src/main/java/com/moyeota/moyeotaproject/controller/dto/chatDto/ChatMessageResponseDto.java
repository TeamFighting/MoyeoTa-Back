package com.moyeota.moyeotaproject.controller.dto.chatDto;

import com.moyeota.moyeotaproject.domain.chatMessage.MessageType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatMessageResponseDto {

    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
    private LocalDateTime time;


    @Builder
    public ChatMessageResponseDto(MessageType type, String roomId, String sender, String message,
                                  LocalDateTime time) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.time = time;
    }
}
