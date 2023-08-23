package com.moyeota.moyeotaproject.controller;

import com.moyeota.moyeotaproject.config.ResponseDto;
import com.moyeota.moyeotaproject.config.ResponseUtil;
import com.moyeota.moyeotaproject.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/room/{senderId}/{receiverId}")
    public ResponseDto createRoom(@RequestParam String name, @PathVariable("senderId") Long user1Id, @PathVariable("receiverId")Long user2Id) {
        return ResponseUtil.SUCCESS("채팅방 생성 성공", chatService.createRoom(name, user1Id, user2Id));
    }

//    @PostMapping("/save/{userId}/{chatRoomId}")
//    public ResponseDto saveMessage(@PathVariable("userId")Long userId, @PathVariable("chatRoomId")Long chatRoomId) {
//        return ResponseUtil.SUCCESS("메시지 저장 성공", chatService.saveMessage(userId, chatRoomId))
//    }

    @GetMapping("/message/{userId}/{chatRoomId}")
    public ResponseDto findAllMessage(@PathVariable("userId")Long userId, @PathVariable("chatRoomId")Long chatRoomId) {
        return ResponseUtil.SUCCESS("메시지 조회 성공", chatService.findAllMessages(userId, chatRoomId));
    }

}
