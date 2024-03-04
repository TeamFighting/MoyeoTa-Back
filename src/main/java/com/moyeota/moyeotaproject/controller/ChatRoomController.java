package com.moyeota.moyeotaproject.controller;

import com.moyeota.moyeotaproject.config.response.ResponseDto;
import com.moyeota.moyeotaproject.config.response.ResponseUtil;
import com.moyeota.moyeotaproject.controller.dto.chatDto.ChatRoomResponseDto;
import com.moyeota.moyeotaproject.service.ChatRoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

//    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    //채팅방 생성 API
//    @ApiOperation(value = "채팅방 생성", notes = "채팅방 생성 API")
//    @PostMapping("/create-room")
//    public ResponseDto<Long> createRoom(@ApiParam(value = "채팅방 이름") @RequestParam("roomName") String roomName) {
//        return ResponseUtil.SUCCESS("채팅방 생성 성공", chatRoomService.createRoom(roomName));
//    }

    //채팅방 메시지 조회 API
//    @ApiOperation(value = "특정 채팅방 메시지 조회", notes = "특정 채팅방 메시지 조회 API")
//    @GetMapping("/messages/{userId}/{chatRoomId}")
//    public ResponseDto findAllMessageDesc(@ApiParam(value = "유저 인덱스 번호") @PathVariable("userId")Long userId, @ApiParam(value = "채팅방 인덱스 번호") @PathVariable("chatRoomId")Long chatRoomId) {
//        return ResponseUtil.SUCCESS("메시지 조회 성공", chatService.findAllMessagesDesc(userId, chatRoomId));
//    }

    @ApiOperation(value = "채팅방 목록 조회", notes = "특정 유저 채팅방 목록 조회 API")
    @GetMapping("/lists")
    public ResponseDto<List<ChatRoomResponseDto>> findAllRoomsByUserIdDesc(HttpServletRequest request) {
        return ResponseUtil.SUCCESS("채팅방 조회 성공", chatRoomService.findAllRoomsByUserDesc(request.getHeader("Authorization")));
    }

    //채팅방에서 나가기 API
    @ApiOperation(value = "채팅방에서 나가기", notes = "채팅방에서 나가기 API")
    @DeleteMapping("/{postId}")
    public ResponseDto deleteRoom(HttpServletRequest request, @ApiParam(value = "모집글 인덱스 번호") @PathVariable("postId") Long postId) {
        chatRoomService.deleteRoom(request.getHeader("Authorization"), postId);
        return ResponseUtil.SUCCESS("채팅방 삭제에 성공하였습니다.", "");
    }

    //메시지 삭제 API
//    @ApiOperation(value = "메시지 삭제", notes = "특정 메시지 삭제 API")
//    @DeleteMapping("/messages/{chatMessageId}")
//    public ResponseDto deleteMessage(@ApiParam(value = "메시지 인덱스 번호") @PathVariable("chatMessageId") Long chatMessageId) {
//        chatService.deleteMessage(chatMessageId);
//        return ResponseUtil.SUCCESS("메시지 삭제에 성공하였습니다.", chatMessageId);
//    }
}
