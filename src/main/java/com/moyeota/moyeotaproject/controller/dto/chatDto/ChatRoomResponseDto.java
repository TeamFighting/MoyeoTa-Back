package com.moyeota.moyeotaproject.controller.dto.chatDto;

import com.moyeota.moyeotaproject.domain.chatRoom.ChatRoom;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatRoomResponseDto {

    private String name;
    private String roomId;
    private int userCount;
    private LocalDateTime createdDate;

    @Builder
    public ChatRoomResponseDto(ChatRoom chatRoom) {
        this.name = chatRoom.getName();
        this.roomId = chatRoom.getRoomId();
        this.userCount = chatRoom.getUserCount();
        this.createdDate = chatRoom.getCreatedDate();
    }
}
