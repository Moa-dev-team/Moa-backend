package com.moa.moa3.dto.chat;

import lombok.Data;

import java.util.List;

@Data
public class CreateChatRequest {
    private List<Long> memberIds;
    private String title;

    public CreateChatRequest(List<Long> memberIds, String title) {
        this.memberIds = memberIds;
        this.title = title;
    }
}