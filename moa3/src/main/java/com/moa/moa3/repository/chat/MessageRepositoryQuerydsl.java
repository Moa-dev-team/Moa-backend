package com.moa.moa3.repository.chat;

import com.moa.moa3.entity.chat.Message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MessageRepositoryQuerydsl {
    int countUnreadMessage(Long chatRoomId, LocalDateTime time);
    Optional<Message> findLastMessage(Long chatRoomId);
    List<Message> findMessagesBeforeCursor(Long chatRoomId, LocalDateTime cursor, int limit);
}
