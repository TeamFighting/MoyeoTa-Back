package com.moyeota.moyeotaproject.controller;

import com.moyeota.moyeotaproject.config.response.ResponseDto;
import com.moyeota.moyeotaproject.config.response.ResponseUtil;
import com.moyeota.moyeotaproject.domain.chatMessage.ChatMessageDto;
import com.moyeota.moyeotaproject.domain.chatMessage.MessageType;
import com.moyeota.moyeotaproject.service.ChatService;
import com.moyeota.moyeotaproject.service.UsersService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

//@Api(tags = "Chat", description = "Chat Controller")
//@ApiResponses({
//        @ApiResponse(code = 200, message = "API 정상 작동"),
//        @ApiResponse(code = 400, message = "BAD REQUEST"),
//        @ApiResponse(code = 404, message = "NOT FOUND"),
//        @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR")
//})
@RequiredArgsConstructor
@RestController
public class ChatController {

    private final SimpMessageSendingOperations messageSendingOperations;
    private final ChatService chatService;
    private final UsersService usersService;

//    @ApiOperation(value = "메시지 보내기", notes = "채팅 API")
    @MessageMapping("/api/chat/messages/{userId}")
    public ResponseDto message(ChatMessageDto message, @DestinationVariable("userId") Long userId) {
        String userName = usersService.findNameByUserId(userId);
        if(message.getType().equals(MessageType.ENTER))
            message.setMessage(userName + "님이 입장하였습니다.");
        messageSendingOperations.convertAndSend("/sub/chatRoom/" + message.getRoomId(), message);
        Long messageId = chatService.saveMessage(userId, message);
        return ResponseUtil.SUCCESS("메시지 전송 완료", messageId);
    }

}
