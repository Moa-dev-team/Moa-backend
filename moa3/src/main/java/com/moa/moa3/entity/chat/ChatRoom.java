package com.moa.moa3.entity.chat;

import com.moa.moa3.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class ChatRoom extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "chat_room_id")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatRoomsMembersJoin> chatRoomsMembersJoins = new ArrayList<>();

    // 채팅방이 사라지면 안에 있던 메시지들도 같이 삭제됩니다.
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();
}
