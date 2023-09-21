package com.moa.moa3.entity.chat;

import com.moa.moa3.entity.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * ChatRoom 과 Member 의 다대다 양방향 매핑을 위한 조인 테이블입니다.<br>
 * 다대다 양방향 매핑은 안티 패턴이라 생각해 더 좋은 방법이 있는지 찾아보겠습니다.
 */

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomsMembersJoin {
    @Id @GeneratedValue
    @Column(name = "chat_rooms_members_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public ChatRoomsMembersJoin(ChatRoom chatRoom, Member member) {
        this.chatRoom = chatRoom;
        this.member = member;
    }
}
