package com.moa.moa3.repository.chat;

import com.moa.moa3.entity.chat.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>, MessageRepositoryQuerydsl {
}
