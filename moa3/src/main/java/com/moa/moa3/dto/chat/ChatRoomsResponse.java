package com.moa.moa3.dto.chat;

import lombok.Data;

import java.util.List;

@Data
public class ChatRoomsResponse {
    List<ChatRoomDto> chatRooms;
}
