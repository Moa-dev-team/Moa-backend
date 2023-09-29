package com.moa.moa3.repository.chat;

import com.moa.moa3.entity.chat.ChatRoom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.moa.moa3.entity.chat.QChatRoom.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomRepositoryQuerydslImpl implements ChatRoomRepositoryQuerydsl{
    private final JPAQueryFactory query;
    @Override
    public Optional<ChatRoom> findByIdWithChatRoomsMembersJoins(Long id) {
        return Optional.ofNullable(
                query
                        .selectFrom(chatRoom)
                        .leftJoin(chatRoom.chatRoomsMembersJoins).fetchJoin()
                        .where(chatRoom.id.eq(id))
                        .fetchOne()
        );
    }

    @Override
    public Optional<ChatRoom> findByIdWithMessages(Long id) {
        return Optional.ofNullable(
                query
                        .selectFrom(chatRoom)
                        .leftJoin(chatRoom.messages).fetchJoin()
                        .where(chatRoom.id.eq(id))
                        .fetchOne()
        );
    }
}
