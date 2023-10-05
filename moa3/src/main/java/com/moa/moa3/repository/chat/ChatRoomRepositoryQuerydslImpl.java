package com.moa.moa3.repository.chat;

import com.moa.moa3.entity.chat.ChatRoom;
import com.moa.moa3.entity.chat.QChatRoomsMembersJoin;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.moa.moa3.entity.chat.QChatRoom.*;
import static com.moa.moa3.entity.chat.QChatRoomsMembersJoin.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomRepositoryQuerydslImpl implements ChatRoomRepositoryQuerydsl{
    private final JPAQueryFactory query;
    @Override
    public Optional<ChatRoom> findByIdWithMembers(Long id) {
        return Optional.ofNullable(
                query
                        .selectFrom(chatRoom)
                        .leftJoin(chatRoom.chatRoomsMembersJoins, chatRoomsMembersJoin).fetchJoin()
                        .leftJoin(chatRoomsMembersJoin.member).fetchJoin()
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
