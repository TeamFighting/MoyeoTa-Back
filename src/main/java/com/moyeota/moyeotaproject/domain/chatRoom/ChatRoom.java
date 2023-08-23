package com.moyeota.moyeotaproject.domain.chatRoom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moyeota.moyeotaproject.domain.BaseTimeEntity;
import com.moyeota.moyeotaproject.domain.chatMessage.ChatMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomId;

    private String name;

    @Enumerated(EnumType.STRING)
    private ChatRoomStatus status;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessages = new ArrayList<>();

    public List<ChatMessage> getMessages() {
        return this.chatMessages;
    }

    public void addMessage(ChatMessage message) {
        chatMessages.add(message);
        message.setChatRoom(this);
    }

    @Builder
    public ChatRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
        this.status = ChatRoomStatus.VALID;
    }

}
