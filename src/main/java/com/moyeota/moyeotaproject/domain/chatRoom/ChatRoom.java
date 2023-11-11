package com.moyeota.moyeotaproject.domain.chatRoom;

import com.moyeota.moyeotaproject.domain.BaseTimeEntity;
import com.moyeota.moyeotaproject.domain.chatMessage.ChatMessage;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
