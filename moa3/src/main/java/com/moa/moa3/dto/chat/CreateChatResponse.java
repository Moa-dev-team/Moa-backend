package com.moa.moa3.dto.chat;

import lombok.Data;

@Data
public class CreateChatResponse {
    private Long roomId;

    public CreateChatResponse(Long roomId) {
        this.roomId = roomId;
    }
}
