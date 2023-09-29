package com.moa.moa3.dto.chat;

import com.moa.moa3.entity.chat.Message;
import lombok.Builder;
import lombok.Data;

@Data
public class MessageDto {
    private MessageType type;
    private String content;
    private Long senderId;
    private Long roomId;

    public MessageDto() {
    }

    public MessageDto(Message message) {
        this.type = message.getType();
        this.content = message.getContent();
        this.senderId = message.getSenderId();
        this.roomId = message.getRoomId();
    }

    @Builder
    public MessageDto(MessageType type, String content, Long senderId, Long roomId) {
        this.type = type;
        this.content = content;
        this.senderId = senderId;
        this.roomId = roomId;
    }
}
