package com.moyeota.moyeotaproject.controller;

import com.moyeota.moyeotaproject.config.response.ResponseDto;
import com.moyeota.moyeotaproject.config.response.ResponseUtil;
import com.moyeota.moyeotaproject.service.ChatService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "ChatRooms", description = "ChatRoom Controller")
@ApiResponses({
        @ApiResponse(code = 200, message = "API 정상 작동"),
        @ApiResponse(code = 400, message = "BAD REQUEST"),
        @ApiResponse(code = 404, message = "NOT FOUND"),
        @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR")
})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat-rooms")
public class ChatRoomController {

    private final ChatService chatService;

    //채팅방 생성 API
    @ApiOperation(value = "채팅방 생성", notes = "채팅방 생성 API")
    @PostMapping("/{senderId}/{receiverId}")
    public ResponseDto createRoom(@ApiParam(value = "유저1 인덱스 번호") @PathVariable("senderId") Long user1Id, @ApiParam(value = "유저2 인덱스 번호") @PathVariable("receiverId")Long user2Id) {
        return ResponseUtil.SUCCESS("채팅방 생성 성공", chatService.createRoom(user1Id, user2Id));
    }

    //채팅방 메시지 조회 API
    @ApiOperation(value = "특정 채팅방 메시지 조회", notes = "특정 채팅방 메시지 조회 API")
    @GetMapping("/messages/{userId}/{chatRoomId}")
    public ResponseDto findAllMessageDesc(@ApiParam(value = "유저 인덱스 번호") @PathVariable("userId")Long userId, @ApiParam(value = "채팅방 인덱스 번호") @PathVariable("chatRoomId")Long chatRoomId) {
        return ResponseUtil.SUCCESS("메시지 조회 성공", chatService.findAllMessagesDesc(userId, chatRoomId));
    }

    //채팅방 목록 조회 API 아직 미완성
    @ApiOperation(value = "채팅방 목록 조회", notes = "특정 유저 채팅방 목록 조회 API")
    @GetMapping("/users/{userId}")
    public ResponseDto findAllRoomsDesc(@ApiParam(value = "유저 인덱스 번호") @PathVariable("userId") Long userId) {
        return ResponseUtil.SUCCESS("채팅방 조회 성공", chatService.findAllRoomsDesc(userId));
    }

    //채팅방 삭제 API
    @ApiOperation(value = "채팅방 삭제", notes = "특정 채팅방 삭제 API")
    @DeleteMapping("/{chatRoomId}")
    public ResponseDto deleteRoom(@ApiParam(value = "채팅방 인덱스 번호") @PathVariable("chatRoomId") Long chatRoomId) {
        chatService.deleteRoom(chatRoomId);
        return ResponseUtil.SUCCESS("채팅방 삭제에 성공하였습니다.", chatRoomId);
    }

    //메시지 삭제 API
    @ApiOperation(value = "메시지 삭제", notes = "특정 메시지 삭제 API")
    @DeleteMapping("/messages/{chatMessageId}")
    public ResponseDto deleteMessage(@ApiParam(value = "메시지 인덱스 번호") @PathVariable("chatMessageId") Long chatMessageId) {
        chatService.deleteMessage(chatMessageId);
        return ResponseUtil.SUCCESS("메시지 삭제에 성공하였습니다.", chatMessageId);
    }
}
