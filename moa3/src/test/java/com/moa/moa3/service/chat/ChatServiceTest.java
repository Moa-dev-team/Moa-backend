package com.moa.moa3.service.chat;

import com.moa.moa3.dto.chat.MessageDto;
import com.moa.moa3.dto.chat.MessageType;
import com.moa.moa3.entity.chat.ChatRoom;
import com.moa.moa3.entity.chat.ChatRoomsMembersJoin;
import com.moa.moa3.entity.member.Member;
import com.moa.moa3.repository.chat.ChatRoomRepository;
import com.moa.moa3.repository.member.MemberRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Rollback(value = false)
@Transactional
class ChatServiceTest {
    @Autowired
    ChatService chatService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    EntityManager em;

    @Test
    void joinChatRoomTest() {
        Member member = new Member("test", "test@com", "test url", "local");
        memberRepository.save(member);

        Long chatRoomId = chatService.createChatRoom();
        chatService.joinChatRoom(chatRoomId, member.getId());
//
        Member findMember = memberRepository.findByIdWithChatRoomsMembersJoins(member.getId()).get();
        assertThat(findMember.getChatRoomsMembersJoins().size()).isEqualTo(1);

        ChatRoom findChatRoom = chatRoomRepository.findByIdWithChatRoomsMembersJoins(chatRoomId).get();
        assertThat(findChatRoom.getChatRoomsMembersJoins().size()).isEqualTo(1);
    }

    @Test
    void sendMessageTest() {
        Long chatRoomId = chatService.createChatRoom();
        MessageDto messageDto = new MessageDto(MessageType.JOIN, "나 등장", "호성", 1L, chatRoomId);
        chatService.sendMessage(chatRoomId, messageDto);

        ChatRoom findChatRoom = chatRoomRepository.findByIdWithMessages(chatRoomId).get();
        assertThat(findChatRoom.getMessages().size()).isEqualTo(1);
    }

    @Test
    void leaveChatTest() {
        Member member = new Member("test", "test@com", "test url", "local");
        memberRepository.save(member);

        Long chatRoomId = chatService.createChatRoom();
        chatService.joinChatRoom(chatRoomId, member.getId());

        em.flush();
        em.clear();

        Member findMember = memberRepository.findByIdWithChatRoomsMembersJoins(member.getId()).get();
        findMember.getChatRoomsMembersJoins().removeIf(
                chatRoomsMembersJoin -> chatRoomsMembersJoin.getChatRoom().getId().equals(chatRoomId)
        );
        ChatRoom findChatRoom = chatRoomRepository.findByIdWithChatRoomsMembersJoins(chatRoomId).get();
        findChatRoom.getChatRoomsMembersJoins().removeIf(
                chatRoomsMembersJoin -> chatRoomsMembersJoin.getMember().getId().equals(member.getId())
        );

        em.flush();
        em.clear();
        assertThat(findMember.getChatRoomsMembersJoins().size()).isEqualTo(0);
        assertThat(findChatRoom.getChatRoomsMembersJoins().size()).isEqualTo(0);
    }
}