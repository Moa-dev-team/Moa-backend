package com.moa.moa3.entity.chat;

import com.moa.moa3.dto.chat.MessageDto;
import com.moa.moa3.dto.chat.MessageType;
import com.moa.moa3.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "message_id")
    private Long id;
    private String content;
    private Long roomId;
    private Long senderId;
    @Enumerated(EnumType.STRING)
    private MessageType type;

    public Message(String content, Long roomId, Long senderId, MessageType type) {
        this.content = content;
        this.roomId = roomId;
        this.senderId = senderId;
        this.type = type;
    }

    public Message(MessageDto messageDto) {
        this.content = messageDto.getContent();
        this.roomId = messageDto.getRoomId();
        this.senderId = messageDto.getSenderId();
        this.type = messageDto.getType();
    }
}
