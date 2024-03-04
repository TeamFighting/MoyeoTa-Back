package com.moyeota.moyeotaproject.domain.chatRoom;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
