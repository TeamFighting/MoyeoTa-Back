package com.moyeota.moyeotaproject.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeota.moyeotaproject.controller.dto.chatDto.ChatMessageResponseDto;
import com.moyeota.moyeotaproject.controller.dto.chatDto.ChatRoomResponseDto;
import com.moyeota.moyeotaproject.domain.chatMessage.ChatMessage;
import com.moyeota.moyeotaproject.domain.chatMessage.ChatMessageDto;
import com.moyeota.moyeotaproject.domain.chatMessage.ChatMessageRepository;
import com.moyeota.moyeotaproject.domain.chatMessage.MessageRoomIdMapping;
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
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final UsersRepository usersRepository;
    private final ChatMessageRepository chatMessageRepository;

    public Long createRoom(Long user1Id, Long user2Id) {
        Users user1 = usersRepository.findById(user1Id).orElseThrow(()
        -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. userId=" + user1Id));
        Users user2 = usersRepository.findById(user2Id).orElseThrow(()
        -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. userId=" + user2Id));
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name(user1.getName() +", " + user2.getName())
                .build();

        return chatRoomRepository.save(chatRoom).getId();
    }

    public ChatRoom findByRoomId(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElseThrow(()
        -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다."));
        return chatRoom;
    }

    public List<ChatMessageResponseDto> findAllMessagesDesc(Long userId, Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(()
        -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. userId" + userId));
        List<ChatMessage> messageList = chatMessageRepository.findByChatRoomOrderByIdDesc(chatRoom);
        List<ChatMessageResponseDto> messageResponseDtoList = new ArrayList<>();
        for (int i=0; i<messageList.size(); i++){
            ChatMessageResponseDto chatMessageResponseDto = ChatMessageResponseDto.builder()
                    .message(messageList.get(i).getMessage())
                    .sender(messageList.get(i).getUser().getName()) // 수정
                    .build();

            messageResponseDtoList.add(chatMessageResponseDto);
        }
        return messageResponseDtoList;
    }

    public List<ChatRoomResponseDto> findAllRoomsDesc(Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow(()
        -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));
        List<MessageRoomIdMapping> roomIds = chatMessageRepository.findRoomIdByUser(user);
        Set<String> roomIdSet = new HashSet<>();
        for (int i=0; i<roomIds.size(); i++)
            roomIdSet.add(roomIds.get(i).getRoomId());
        Iterator iterator = roomIdSet.iterator();
        List<ChatRoomResponseDto> chatRoomResponseDtoList = new ArrayList<>();
        while(iterator.hasNext()){
            String roomId = (String) iterator.next();
            ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElseThrow(()
            -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다."));
            List<ChatMessage> chatMessageList = chatMessageRepository.findChatMessage(roomId);
            ChatMessage chatMessage = chatMessageList.get(0);
            ChatRoomResponseDto responseDto = ChatRoomResponseDto.builder()
                    .name(chatRoom.getName())
                    .createdDate(chatMessage.getCreatedDate())
                    .message(chatMessage)
                    .build();
            chatRoomResponseDtoList.add(responseDto);
        }
        return chatRoomResponseDtoList;
    }

    public Long saveMessage(Long userId, ChatMessageDto message) {
        Users user = usersRepository.findById(userId).orElseThrow();
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(message.getRoomId()).orElseThrow();
        ChatMessage chatMessage = ChatMessage.builder()
                .message(message.getMessage())
                .type(message.getType())
                .sender(user.getName())
                .roomId(message.getRoomId())
                .user(user)
                .chatRoom(chatRoom).build();
        return chatMessageRepository.save(chatMessage).getId();
    }

    public void deleteRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(()
        -> new IllegalArgumentException("해당 채팅방이 없습니다. id=" + chatRoomId));
        chatRoomRepository.delete(chatRoom);
    }

    public void deleteMessage(Long chatMessageId) {
        ChatMessage chatMessage = chatMessageRepository.findById(chatMessageId).orElseThrow(()
        -> new IllegalArgumentException("해당 메시지가 없습니다. id=" + chatMessageId));
        chatMessageRepository.delete(chatMessage);
    }
}
