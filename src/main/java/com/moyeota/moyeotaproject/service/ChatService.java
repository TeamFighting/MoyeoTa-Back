package com.moyeota.moyeotaproject.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeota.moyeotaproject.controller.dto.ChatMessageResponseDto;
import com.moyeota.moyeotaproject.domain.chatMessage.ChatMessage;
import com.moyeota.moyeotaproject.domain.chatMessage.ChatMessageDto;
import com.moyeota.moyeotaproject.domain.chatMessage.ChatMessageRepository;
import com.moyeota.moyeotaproject.domain.chatRoom.ChatRoom;
import com.moyeota.moyeotaproject.domain.chatRoom.ChatRoomRepository;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ObjectMapper mapper;
    private final ChatRoomRepository chatRoomRepository;
    private final UsersRepository usersRepository;
    private final ChatMessageRepository chatMessageRepository;

    public Long createRoom(String name, Long user1Id, Long user2Id) {
        Users user1 = usersRepository.findById(user1Id).orElseThrow();
        Users user2 = usersRepository.findById(user2Id).orElseThrow();
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name(user1.getName() +", " + user2.getName())
                .build();

        return chatRoomRepository.save(chatRoom).getId();
    }

    public ChatRoom findByRoomId(String roomId) {
        return chatRoomRepository.findByRoomId(roomId);
    }

    public List<ChatMessageResponseDto> findAllMessages(Long userId, Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();
        List<ChatMessage> messageList = chatMessageRepository.findByChatRoom(chatRoom);
        List<ChatMessageResponseDto> messageResponseDtoList = new ArrayList<>();
        for (int i=0; i<messageList.size(); i++){
            ChatMessageResponseDto chatMessageResponseDto = ChatMessageResponseDto.builder()
                    .message(messageList.get(i).getMessage())
                    .sender(messageList.get(i).getSender())
                    .build();

            messageResponseDtoList.add(chatMessageResponseDto);
        }
        return messageResponseDtoList;
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try{
            TextMessage textMessage = new TextMessage(mapper.writeValueAsString(message));
            session.sendMessage(textMessage);
            String payload = textMessage.getPayload();
            ChatMessageDto chatMessage = (ChatMessageDto) message;
            saveMessage(chatMessage.getUserId(), chatMessage.getChatRoomId(), chatMessage);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public Long saveMessage(Long userId, Long chatRoomId, ChatMessageDto message) {
        Users user = usersRepository.findById(userId).orElseThrow();
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();
        ChatMessage chatMessage = ChatMessage.builder()
                .message(message.getMessage())
                .type(message.getType())
                .sender(message.getSender())
                .roomId(message.getRoomId())
                .user(user)
                .chatRoom(chatRoom).build();
        return chatMessageRepository.save(chatMessage).getId();

    }
}
