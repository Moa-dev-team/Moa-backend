package com.moa.moa3.controller.user;

import com.moa.moa3.dto.chat.MessageDto;
import com.moa.moa3.dto.chat.MessageType;
import com.moa.moa3.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * session 이 끊어졌을때 알림을 주기 위해 쓰입니다.
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        Long memberId = (Long) headerAccessor.getSessionAttributes().get("memberId");
        Long roomId = (Long) headerAccessor.getSessionAttributes().get("roomId");

        String destination = "/topic/" + roomId;

        if(memberId != null) {
            MessageDto messageDto = new MessageDto();
            messageDto.setType(MessageType.LEAVE);
            messageDto.setSenderId(memberId);
            chatService.setLastAccessTime(roomId, memberId);
            messagingTemplate.convertAndSend(destination, messageDto);
        }
    }
}