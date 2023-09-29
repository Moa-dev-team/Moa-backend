package com.moa.moa3.controller.user;


import com.moa.moa3.dto.chat.MessageDto;
import com.moa.moa3.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat.sendMessage")
    public MessageDto sendMessage(@Payload MessageDto messageDto) {
        Long roomId = messageDto.getRoomId();
        String destination = "/topic/" + roomId;
        chatService.sendMessage(roomId, messageDto);
        messagingTemplate.convertAndSend(destination, messageDto);
        return messageDto;
    }

    @MessageMapping("/chat.addUser")
    public MessageDto addUser(@Payload MessageDto messageDto,
                              SimpMessageHeaderAccessor headerAccessor) {
        Long roomId = messageDto.getRoomId();
        String destination = "/topic/" + roomId;

        headerAccessor.getSessionAttributes().put("memberId", messageDto.getSenderId());
        headerAccessor.getSessionAttributes().put("roomId", messageDto.getRoomId());
        headerAccessor.getSessionAttributes().put("name", messageDto.getSender());

        messagingTemplate.convertAndSend(destination, messageDto);
        return messageDto;
    }
}
