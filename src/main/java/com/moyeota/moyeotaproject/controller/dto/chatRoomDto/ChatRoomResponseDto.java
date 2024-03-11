package com.moyeota.moyeotaproject.controller.dto.chatRoomDto;

import com.moyeota.moyeotaproject.domain.chatRoom.ChatRoom;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatRoomResponseDto {

    private Long postId;
    private String roomName;
    private String roomId;
    private int userCount;
    private LocalDateTime createdDate;

    @Builder
    public ChatRoomResponseDto(ChatRoom chatRoom, Posts posts) {
        this.postId = posts.getId();
        this.roomName = chatRoom.getName();
        this.roomId = chatRoom.getRoomId();
        this.userCount = chatRoom.getUserCount();
        this.createdDate = chatRoom.getCreatedDate();
    }
}
