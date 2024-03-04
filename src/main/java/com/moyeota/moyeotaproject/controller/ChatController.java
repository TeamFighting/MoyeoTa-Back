//package com.moyeota.moyeotaproject.controller;
//
//import com.moyeota.moyeotaproject.config.response.ResponseDto;
//import com.moyeota.moyeotaproject.config.response.ResponseUtil;
//import com.moyeota.moyeotaproject.domain.chatMessage.ChatMessageDto;
//import com.moyeota.moyeotaproject.service.ChatRoomService;
//import com.moyeota.moyeotaproject.service.ChatService;
//import com.moyeota.moyeotaproject.service.UsersService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//import org.springframework.web.bind.annotation.RestController;
//
////@Api(tags = "Chat", description = "Chat Controller")
////@ApiResponses({
////        @ApiResponse(code = 200, message = "API 정상 작동"),
////        @ApiResponse(code = 400, message = "BAD REQUEST"),
////        @ApiResponse(code = 404, message = "NOT FOUND"),
////        @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR")
////})
//@RequiredArgsConstructor
//@RestController
//public class ChatController {
//
//    private final SimpMessageSendingOperations messageSendingOperations;
//    private final ChatService chatService;
//    private final ChatRoomService chatRoomService;
//    private final UsersService usersService;
//
////    @ApiOperation(value = "메시지 보내기", notes = "채팅 API")
////    @SendTo("/sub/public")
//    @MessageMapping("chat/enter/users/{userId}/chat-rooms/{chatRoomId}")
//    public ResponseDto<Long> enterUser(@DestinationVariable("userId") Long userId, @DestinationVariable("chatRoomId") Long chatRoomId) {
//        String userName = usersService.findNameByUserId(userId);
//        Long chatRoomAndUserId = chatRoomService.addUser(chatRoomId, userId);
//        messageSendingOperations.convertAndSend("/sub/chatRoom/" + chatRoomId, userName + "님이 입장하였습니다.");
//        return ResponseUtil.SUCCESS("채팅방 입장 완료", chatRoomAndUserId);
//    }
//
//    @MessageMapping("chat/send-messages/users/{userId}/chat-rooms/{chatRoomId}")
//    public ResponseDto<Long> sendMessage(ChatMessageDto message, @DestinationVariable("userId") Long userId, @DestinationVariable("chatRoomId") Long chatRoomId) {
//        String userName = usersService.findNameByUserId(userId);
//        Long messageId = chatService.saveMessage(userId, chatRoomId, message);
//        messageSendingOperations.convertAndSend("/sub/chatRoom/" + chatRoomId, message);
//        return ResponseUtil.SUCCESS("메시지 전송 완료", messageId);
//    }
//
//    @MessageMapping("/chat/exit/users/{userId}/chat-rooms/{chatRoomId}")
//    public ResponseDto<String> exitUser(@DestinationVariable("userId") Long userId, @DestinationVariable("chatRoomId") Long chatRoomId) {
//        String userName = usersService.findNameByUserId(userId);
//        chatRoomService.deleteUser(chatRoomId, userId);
//        messageSendingOperations.convertAndSend("/sub/chatRoom/" + chatRoomId, userName + "님이 퇴장하였습니다.");
//        return ResponseUtil.SUCCESS("채팅방 퇴장 완료", "");
//    }
//
//}
