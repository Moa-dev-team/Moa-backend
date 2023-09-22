package com.moa.moa3.dto.chat;

import lombok.Data;

import java.util.List;

@Data
public class CreateChatRequest {
    private Long roomId;
    private List<Long> memberIds;
}