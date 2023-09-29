package com.moa.moa3.dto.chat;

import com.moa.moa3.entity.chat.Message;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LastChatDto {
    private String content;
    LocalDateTime time;

    public LastChatDto(Message message) {
        if (message == null) {
            this.content = "";
            this.time = null;
        } else {
            this.content = message.getContent();
            this.time = message.getCreatedAt();
        }
    }
}
