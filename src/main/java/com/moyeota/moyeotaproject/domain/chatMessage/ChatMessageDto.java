package com.moyeota.moyeotaproject.domain.chatMessage;

import com.moyeota.moyeotaproject.domain.chatRoom.ChatRoom;
import com.moyeota.moyeotaproject.domain.users.Users;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "채팅 전송")
public class ChatMessageDto {

    @ApiModelProperty(value = "메시지 내용", example = "안녕! 잘 지냈어?", required = true)
    private String message;

//    @ApiModelProperty(value = "채팅방 아이디", example = "93615d98-4b21-4bb3-9e41-530689440589", required = true)
//    private String roomId;

    @Builder
    public ChatMessageDto(String message) {
        this.message = message;
//        this.roomId = roomId;
    }

    public ChatMessage toEntity(Users user, ChatRoom chatRoom) {
        return ChatMessage.builder()
                .message(message)
                .user(user)
                .chatRoom(chatRoom)
                .build();

    }

}
