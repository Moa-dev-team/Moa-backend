package com.moa.moa3.dto.chat;

import lombok.Data;

import java.util.List;

@Data
public class InviteRequest {
    List<Long> memberIds;
    private Long roomId;
}
