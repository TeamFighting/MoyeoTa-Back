package com.moyeota.moyeotaproject.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeota.moyeotaproject.domain.chatMessage.ChatMessageDto;
import com.moyeota.moyeotaproject.domain.chatMessage.MessageType;
import com.moyeota.moyeotaproject.domain.chatRoom.ChatRoom;
import com.moyeota.moyeotaproject.domain.chatRoom.ChatRoomDto;
import com.moyeota.moyeotaproject.domain.chatRoom.ChatRoomRepository;
import com.moyeota.moyeotaproject.domain.users.Entity.UsersRepository;
import com.moyeota.moyeotaproject.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ChatService chatService;

    private final UsersRepository usersRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);

        Map<String, Object> map = objectMapper.readValue(payload, new TypeReference<HashMap<String, Object>>() {
        });

        ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                .type(MessageType.valueOf(String.valueOf(map.get("type"))))
                .message(String.valueOf(map.get("message")))
                .roomId(String.valueOf(map.get("roomId")))
                .sender(String.valueOf(map.get("sender")))
                .userId(Long.parseLong(String.valueOf(map.get("userId"))))
                .chatRoomId(Long.parseLong(String.valueOf(map.get("chatRoomId"))))
                .build();

        log.info("session {}", chatMessageDto.toString());

        ChatRoom chatRoom = chatService.findByRoomId(chatMessageDto.getRoomId());
        ChatRoomDto roomDto = ChatRoomDto.builder().roomId(chatRoom.getRoomId()).name(chatRoom.getName()).build();
        log.info("room {}", roomDto.toString());

        roomDto.handleActions(session, chatMessageDto, chatService);
    }
}
