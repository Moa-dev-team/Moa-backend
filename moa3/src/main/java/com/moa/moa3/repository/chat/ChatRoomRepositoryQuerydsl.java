package com.moa.moa3.repository.chat;

import com.moa.moa3.entity.chat.ChatRoom;

import java.util.Optional;

public interface ChatRoomRepositoryQuerydsl {
    Optional<ChatRoom> findByIdWithMembers(Long id);
    Optional<ChatRoom> findByIdWithMessages(Long id);
}
