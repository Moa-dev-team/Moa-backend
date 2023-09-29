package com.moa.moa3.controller.user;

import com.moa.moa3.dto.chat.CreateChatRequest;
import com.moa.moa3.dto.chat.CreateChatResponse;
import com.moa.moa3.dto.chat.InviteRequest;
import com.moa.moa3.security.MemberDetails;
import com.moa.moa3.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

// 현재 인증, 인가 절차를 진행하고 있지 않지만 향후 포함될 수 있습니다.
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    private static final LocalDateTime MAX_DATE = LocalDateTime.of(2200, 1, 1, 0, 0);


    @PostMapping("/create")
    public ResponseEntity createChatRoom(@RequestBody CreateChatRequest createChatRequest) {
        Long roomId = chatService.createChatRoom();
        chatService.addChatMembers(roomId, createChatRequest.getMemberIds());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CreateChatResponse(roomId));
    }

    @GetMapping("rooms")
    public ResponseEntity getChatRooms() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberDetails principal = (MemberDetails) authentication.getPrincipal();
        Long memberId = principal.getMemberId();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(chatService.getChatRooms(memberId));
    }

    @GetMapping("/messages")
    public ResponseEntity getChatMessages(
            @RequestParam Long roomId,
            @RequestParam(required = false)LocalDateTime cursor,
            @RequestParam(required = false, defaultValue = "20")Integer limit
            ) {
        if (cursor == null) {
            cursor = MAX_DATE;
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(chatService.getMessages(roomId, cursor, limit));
    }

    @PostMapping("/invite")
    public ResponseEntity inviteMember(@RequestBody InviteRequest inviteRequest) {
        chatService.addChatMembers(inviteRequest.getRoomId(), inviteRequest.getMemberIds());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("invite success");
    }

    @GetMapping("/leave")
    public ResponseEntity leaveChatRoom(
            @RequestParam Long roomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberDetails principal = (MemberDetails) authentication.getPrincipal();
        Long memberId = principal.getMemberId();
        chatService.leaveChatRoom(roomId, memberId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("leave success");
    }
}
