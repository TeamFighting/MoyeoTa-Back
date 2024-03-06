package com.moyeota.moyeotaproject.domain.chatRoom;

import com.moyeota.moyeotaproject.domain.BaseTimeEntity;
import com.moyeota.moyeotaproject.domain.chatRoomAndUsers.ChatRoomAndUsers;
import com.moyeota.moyeotaproject.domain.posts.Posts;
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
import javax.persistence.OneToOne;
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

    private int userCount;

    @Enumerated(EnumType.STRING)
    private ChatRoomStatus status;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatRoomAndUsers> chatRoomAndUsersList = new ArrayList<>();

//    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
//    private List<ChatMessage> chatMessages = new ArrayList<>();

    @OneToOne(mappedBy = "chatRoom")
    private Posts post;

//    public List<ChatMessage> getMessages() {
//        return this.chatMessages;
//    }

//    public void addMessage(ChatMessage message) {
//        chatMessages.add(message);
//        message.setChatRoom(this);
//    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    @Builder
    public ChatRoom(String roomId, String name, int userCount) {
        this.roomId = roomId;
        this.name = name;
        this.userCount = userCount;
        this.status = ChatRoomStatus.VALID;

    }

}
