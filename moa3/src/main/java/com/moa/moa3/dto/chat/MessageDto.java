package com.moa.moa3.dto.chat;

import lombok.Data;

@Data
public class MessageDto {
    private MessageType type;
    private String content;
    private String sender;
}
