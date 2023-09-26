package com.moa.moa3.entity.chat;

import com.moa.moa3.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * ChatRoom 과 Member 은 다대다 양방향 매핑이기 때문에 사용에 주의해야합니다.
 */
@Entity
@Getter
public class ChatRoom extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "chat_room_id")
    private Long id;
    private String title;


    // chatRoomsMemberJoin 을 더하는 로직은 ChatRoomService 에 있습니다.
    // Member 객체도 같이 넣어주어야 하기 때문입니다.
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoomsMembersJoin> chatRoomsMembersJoins = new ArrayList<>();

    // 채팅방이 사라지면 안에 있던 메시지들도 같이 삭제됩니다.
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();
}
