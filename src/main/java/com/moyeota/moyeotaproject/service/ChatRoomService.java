package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.controller.dto.chatDto.ChatRoomResponseDto;
import com.moyeota.moyeotaproject.domain.chatMessage.ChatMessage;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UsersRepository usersRepository;
    private final ChatRoomAndUsersRepository chatRoomAndUsersRepository;
    private final ChatMessageRepository chatMessageRepository;

    public Long createRoom(String roomName) {
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name(roomName)
                .userCount(0)
                .build();

        return chatRoomRepository.save(chatRoom).getId();
    }

    public ChatRoom findByRoomId(long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(()
                -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다."));
        return chatRoom;
    }

    public List<ChatRoomResponseDto> findAllRoomsByUserIdDesc(Long userId) {
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
                    .roomId(chatRoom.getRoomId())
                    .userCount(chatRoom.getUserCount())
                    .createdDate(chatMessage.getCreatedDate())
                    .message(chatMessage)
                    .build();
            chatRoomResponseDtoList.add(responseDto);
        }
        return chatRoomResponseDtoList;
    }

    public void deleteRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(()
                -> new IllegalArgumentException("해당 채팅방이 없습니다. id=" + chatRoomId));
        chatRoomRepository.delete(chatRoom);
    }

    //채팅방 유저 추가
    public Long addUser(long chatRoomId, long userId) {
        Users user = usersRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(
                () -> new IllegalArgumentException("해당 채팅방이 없습니다. id=" + chatRoomId));
        increaseUser(chatRoomId);
        ChatRoomAndUsers chatRoomAndUsers = ChatRoomAndUsers.builder().user(user).chatRoom(chatRoom).build();
        return chatRoomAndUsersRepository.save(chatRoomAndUsers).getId();
    }

    //채팅방 유저 퇴장
    public void deleteUser(long chatRoomId, long userId) {
        Users user = usersRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(
                () -> new IllegalArgumentException("해당 채팅방이 없습니다. id=" + chatRoomId));
        decreaseUser(chatRoomId);
        ChatRoomAndUsers chatRoomAndUsers = chatRoomAndUsersRepository.findByChatRoomAndUser(chatRoom, user);
        chatRoomAndUsersRepository.delete(chatRoomAndUsers);
    }

    //채팅방 인원 + 1
    public void increaseUser(long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(
                () -> new IllegalArgumentException("해당 채팅방이 없습니다. id= " + chatRoomId));
        chatRoom.setUserCount(chatRoom.getUserCount() + 1);
    }

    //채팅방 인원 - 1
    public void decreaseUser(long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(
                () -> new IllegalArgumentException("해당 채팅방이 없습니다. id= " + chatRoomId));
        chatRoom.setUserCount(chatRoom.getUserCount() - 1);
    }


}
