package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.controller.dto.chatDto.ChatMessageResponseDto;
import com.moyeota.moyeotaproject.controller.dto.chatDto.ChatRoomResponseDto;
import com.moyeota.moyeotaproject.domain.chatMessage.ChatMessage;
import com.moyeota.moyeotaproject.domain.chatMessage.ChatMessageDto;
import com.moyeota.moyeotaproject.domain.chatMessage.ChatMessageRepository;
import com.moyeota.moyeotaproject.domain.chatMessage.MessageRoomIdMapping;
import com.moyeota.moyeotaproject.domain.chatRoom.ChatRoom;
import com.moyeota.moyeotaproject.domain.chatRoom.ChatRoomRepository;
import com.moyeota.moyeotaproject.domain.chatRoomAndUsers.ChatRoomAndUsers;
import com.moyeota.moyeotaproject.domain.chatRoomAndUsers.ChatRoomAndUsersRepository;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final UsersRepository usersRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomAndUsersRepository chatRoomAndUsersRepository;

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

    public Long saveMessage(Long userId, Long chatRoomId, ChatMessageDto message) {
        Users user = usersRepository.findById(userId).orElseThrow();
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(
                () -> new IllegalArgumentException("해당 채팅방이 없습니다. id=" + chatRoomId));
        ChatMessage chatMessage = ChatMessage.builder()
                .message(message.getMessage())
                .sender(user.getName())
                .user(user)
                .chatRoom(chatRoom).build();
        return chatMessageRepository.save(chatMessage).getId();
    }

    public void deleteMessage(Long chatMessageId) {
        ChatMessage chatMessage = chatMessageRepository.findById(chatMessageId).orElseThrow(()
        -> new IllegalArgumentException("해당 메시지가 없습니다. id=" + chatMessageId));
        chatMessageRepository.delete(chatMessage);
    }


}
