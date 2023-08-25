package com.moyeota.moyeotaproject.controller;

import com.moyeota.moyeotaproject.config.ResponseDto;
import com.moyeota.moyeotaproject.config.ResponseUtil;
import com.moyeota.moyeotaproject.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chattings")
public class ChatController {

    private final ChatService chatService;

    //채팅방 생성 API
    @PostMapping("/rooms/{senderId}/{receiverId}")
    public ResponseDto createRoom(@RequestParam String name, @PathVariable("senderId") Long user1Id, @PathVariable("receiverId")Long user2Id) {
        return ResponseUtil.SUCCESS("채팅방 생성 성공", chatService.createRoom(name, user1Id, user2Id));
    }

    //채팅방 메시지 조회 API
    @GetMapping("/messages/{userId}/{chatRoomId}")
    public ResponseDto findAllMessageDesc(@PathVariable("userId")Long userId, @PathVariable("chatRoomId")Long chatRoomId) {
        return ResponseUtil.SUCCESS("메시지 조회 성공", chatService.findAllMessagesDesc(userId, chatRoomId));
    }

    //채팅방 목록 조회 API 아직 미완성
    @GetMapping("/rooms/{userId}")
    public ResponseDto findAllRoomsDesc(@PathVariable("userId") Long userId) {
        return ResponseUtil.SUCCESS("채팅방 조회 성공", chatService.findAllRoomsDesc(userId));
    }

    //채팅방 삭제 API
    @DeleteMapping("/rooms/{chatRoomId}")
    public ResponseDto deleteRoom(@PathVariable("chatRoomId") Long chatRoomId) {
        chatService.deleteRoom(chatRoomId);
        return ResponseUtil.SUCCESS("채팅방 삭제에 성공하였습니다.", chatRoomId);
    }

    //메시지 삭제 API
    @DeleteMapping("/messages/{chatMessageId}")
    public ResponseDto deleteMessage(@PathVariable("chatMessageId") Long chatMessageId) {
        chatService.deleteMessage(chatMessageId);
        return ResponseUtil.SUCCESS("메시지 삭제에 성공하였습니다.", chatMessageId);
    }
}
