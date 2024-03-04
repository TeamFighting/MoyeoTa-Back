//package com.moyeota.moyeotaproject.domain.chatMessage;
//
//import com.moyeota.moyeotaproject.domain.BaseTimeEntity;
//import com.moyeota.moyeotaproject.domain.chatRoom.ChatRoom;
//import com.moyeota.moyeotaproject.domain.users.Users;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import javax.persistence.*;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@Entity
//public class ChatMessage extends BaseTimeEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String message;
//
//    private String sender;
//
//    @Enumerated(EnumType.STRING)
//    private MessageStatus status;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "chatRoomId")
//    private ChatRoom chatRoom;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "userId")
//    private Users user;
//
//    public void setChatRoom(ChatRoom chatRoom){
//        this.chatRoom = chatRoom;
//        chatRoom.getChatMessages().add(this);
//    }
//
//    public void setUser(Users user) {
//        this.user = user;
//        user.getChatMessages().add(this);
//    }
//
//    @Builder
//    public ChatMessage(String message, String sender, Users user, ChatRoom chatRoom) {
//        this.message = message;
//        this.sender = sender;
//        this.setChatRoom(chatRoom);
//        this.setUser(user);
//        this.status = MessageStatus.VALID;
//    }
//}
