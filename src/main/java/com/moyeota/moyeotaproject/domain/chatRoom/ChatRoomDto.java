package com.moyeota.moyeotaproject.domain.chatRoom;

import com.moyeota.moyeotaproject.domain.chatMessage.ChatMessageDto;
import com.moyeota.moyeotaproject.service.ChatService;
import com.moyeota.moyeotaproject.domain.chatMessage.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
public class ChatRoomDto {

    private String name;
    private String roomId;

    @Builder
    public ChatRoomDto(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

}
