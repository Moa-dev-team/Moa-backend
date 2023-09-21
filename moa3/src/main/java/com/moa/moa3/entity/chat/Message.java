package com.moa.moa3.entity.chat;

import com.moa.moa3.dto.chat.MessageType;
import com.moa.moa3.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Message extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "message_id")
    private Long id;
    private String content;
    private Long roomId;
    private Long senderId;
    @Enumerated(EnumType.STRING)
    private MessageType type;
}
