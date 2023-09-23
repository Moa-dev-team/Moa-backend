package com.moa.moa3.controller.user;

import com.moa.moa3.dto.chat.CreateChatRequest;
import com.moa.moa3.dto.chat.CreateChatResponse;
import com.moa.moa3.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/create")
    public ResponseEntity createChatRoom(@RequestBody CreateChatRequest createChatRequest) {
        Long roomId = chatService.createChatRoom();
        chatService.addChatMembers(roomId, createChatRequest.getMemberIds());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CreateChatResponse(roomId));
    }
}
