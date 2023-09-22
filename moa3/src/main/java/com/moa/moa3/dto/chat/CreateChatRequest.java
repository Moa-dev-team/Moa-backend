package com.moa.moa3.dto.chat;

import lombok.Data;

import java.util.List;

@Data
public class CreateChatRequest {
    private List<Long> memberIds;
}