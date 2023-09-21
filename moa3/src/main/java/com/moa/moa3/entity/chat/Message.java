package com.moa.moa3.entity.chat;

import com.moa.moa3.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Message extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "message_id")
    private Long id;
    private String content;
    private Long roomId;
    private Long senderId;
}
