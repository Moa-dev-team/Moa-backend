package com.moa.moa3.repository.chat;

import com.moa.moa3.entity.chat.Message;
import com.moa.moa3.entity.chat.QMessage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.moa.moa3.entity.chat.QMessage.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageRepositoryQuerydslImpl implements MessageRepositoryQuerydsl{
    private final JPAQueryFactory query;

    @Override
    public int countUnreadMessage(Long chatRoomId, LocalDateTime time) {
        return query
                .selectFrom(message)
                .where(message.roomId.eq(chatRoomId), message.createdAt.after(time))
                .limit(100)
                .fetch().size();
    }

    @Override
    public Optional<Message> findLastMessage(Long chatRoomId) {
        return Optional.ofNullable(
                query
                        .selectFrom(message)
                        .where(message.roomId.eq(chatRoomId))
                        .orderBy(message.createdAt.desc())
                        .limit(1)
                        .fetchOne()
        );
    }

    @Override
    public List<Message> findMessagesBeforeCursor(Long chatRoomId, LocalDateTime cursor, int limit) {
        return query
                .selectFrom(message)
                .where(message.roomId.eq(chatRoomId), message.createdAt.before(cursor))
                .orderBy(message.createdAt.desc())
                .limit(limit)
                .fetch();
    }
}
