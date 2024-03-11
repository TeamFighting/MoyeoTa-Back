package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.config.jwtConfig.JwtTokenProvider;
import com.moyeota.moyeotaproject.controller.dto.chatRoomDto.ChatRoomResponseDto;
import com.moyeota.moyeotaproject.domain.chatRoom.ChatRoom;
import com.moyeota.moyeotaproject.domain.chatRoom.ChatRoomRepository;
import com.moyeota.moyeotaproject.domain.chatRoomAndUsers.ChatRoomAndUsers;
import com.moyeota.moyeotaproject.domain.chatRoomAndUsers.ChatRoomAndUsersRepository;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.posts.PostsRepository;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final PostsRepository postsRepository;
    private final UsersService usersService;
    private JwtTokenProvider jwtTokenProvider;
//    private final ChatMessageRepository chatMessageRepository;

    public Long createRoom(String roomName, String roomId) {
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(roomId)
                .name(roomName)
                .userCount(0)
                .build();

        return chatRoomRepository.save(chatRoom).getId();
    }


    public List<ChatRoomResponseDto> findAllRoomsByUserDesc(String accessToken) {
        Users user = usersService.getUserByToken(accessToken);
        List<ChatRoomAndUsers> chatRoomList = chatRoomAndUsersRepository.findAllByUserOrderByModifiedDateDesc(user);
        List<ChatRoomResponseDto> chatRoomResponseDtoList = new ArrayList<>();
        for (int i = 0; i < chatRoomList.size(); i++) {
            ChatRoom chatRoom = chatRoomList.get(i).getChatRoom();
            ChatRoomResponseDto chatRoomResponseDto = ChatRoomResponseDto.builder().chatRoom(chatRoom).posts(chatRoom.getPost()).build();
            chatRoomResponseDtoList.add(chatRoomResponseDto);
        }
        return chatRoomResponseDtoList;
    }

    public void deleteRoom(String accessToken, Long postId) {
        Users user = usersService.getUserByToken(accessToken);
        Posts post = postsRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 모집글이 없습니다. id=" + postId)
        );
        deleteUser(post.getChatRoom().getId(), user.getId());
    }

    //채팅방 유저 추가
    public Long addUser(Long chatRoomId, Long userId) {
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
        Optional<ChatRoomAndUsers> chatRoomAndUsers = chatRoomAndUsersRepository.findByChatRoomAndUser(chatRoom, user);
        if (chatRoomAndUsers.isEmpty()) {
            throw new IllegalArgumentException("이미 참가 취소되었습니다.");
        }
        decreaseUser(chatRoomId);
        chatRoomAndUsersRepository.delete(chatRoomAndUsers.get());
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
