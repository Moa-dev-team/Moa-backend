package com.moa.moa3.dto.chat;

import lombok.Builder;
import lombok.Data;

@Data
public class MessageDto {
    private MessageType type;
    private String content;
    private String sender;
    private Long senderId;
    private Long roomId;

    public MessageDto() {
    }

    @Builder
    public MessageDto(MessageType type, String content, String sender, Long senderId, Long roomId) {
        this.type = type;
        this.content = content;
        this.sender = sender;
        this.senderId = senderId;
        this.roomId = roomId;
    }
}
