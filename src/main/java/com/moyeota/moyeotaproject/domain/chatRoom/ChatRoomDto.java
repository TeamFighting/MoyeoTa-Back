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
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoomDto(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public void handleActions(WebSocketSession session, ChatMessageDto chatMessage, ChatService chatService) {
        if(chatMessage.getType().equals(MessageType.ENTER)){
//            sessions.add(session);
            chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다.");
        }
        sessions.add(session);
        sendMessage(chatMessage, chatService);
    }

    public <T> void sendMessage(T message, ChatService chatTestService) {
        sessions.parallelStream().forEach(session -> chatTestService.sendMessage(session, message));
    }

}
