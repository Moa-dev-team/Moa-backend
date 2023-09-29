package com.moa.moa3.dto.chat;

import lombok.Data;

@Data
public class ChatRoomDto {
    private Long id;
    private String title;
    private int unreadCount;
    private LastChatDto lastChat;

    public ChatRoomDto(Long id, String title, int unreadCount, LastChatDto lastChat) {
        this.id = id;
        this.title = title;
        this.unreadCount = unreadCount;
        this.lastChat = lastChat;
    }
}
