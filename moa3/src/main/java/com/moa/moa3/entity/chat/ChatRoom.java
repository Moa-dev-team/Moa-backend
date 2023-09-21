package com.moa.moa3.entity.chat;

import com.moa.moa3.entity.BaseEntity;
import com.moa.moa3.entity.member.Member;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ChatRoom extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "chat_room_id")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "chat_rooms_members_id")
    private List<ChatRoomsMembers> ChatRoomsMembers = new ArrayList<>();

    // 채팅방이 사라지면 안에 있던 메시지들도 같이 삭제됩니다.
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    
}
