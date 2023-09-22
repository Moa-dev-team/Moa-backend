package com.moa.moa3.service.chat;

import com.moa.moa3.dto.chat.MessageDto;
import com.moa.moa3.dto.chat.MessageType;
import com.moa.moa3.entity.chat.ChatRoom;
import com.moa.moa3.entity.chat.ChatRoomsMembersJoin;
import com.moa.moa3.entity.member.Member;
import com.moa.moa3.repository.chat.ChatRoomRepository;
import com.moa.moa3.repository.chat.ChatRoomsMembersJoinRepository;
import com.moa.moa3.repository.member.MemberRepository;
import com.moa.moa3.service.member.MemberService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    ChatRoomsMembersJoinRepository chatRoomsMembersJoinRepository;

    @Autowired
    EntityManager em;

    @Test
    void joinChatRoomTest() {
        Member member = new Member("test", "test@com", "test url", "local");
        memberRepository.save(member);

        Long chatRoomId = chatService.createChatRoom();
        chatService.addChatMembers(chatRoomId, List.of(member.getId()));
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
        chatService.addChatMembers(chatRoomId, List.of(member.getId()));

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

    @Test
    void deleteChatRoomMembersJoin() {
        Member member = new Member("test", "test@com", "test url", "local");
        memberRepository.save(member);

        Long chatRoomId = chatService.createChatRoom();
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).get();

        ChatRoomsMembersJoin chatRoomsMembersJoin = new ChatRoomsMembersJoin(chatRoom, member);
        member.getChatRoomsMembersJoins().add(chatRoomsMembersJoin);
        chatRoom.getChatRoomsMembersJoins().add(chatRoomsMembersJoin);
        chatRoomsMembersJoinRepository.save(chatRoomsMembersJoin);
        em.flush();
        em.clear();

        chatRoomsMembersJoinRepository.delete(chatRoomsMembersJoin);
        em.flush();
        em.clear();

        ChatRoom findChatRoom = chatRoomRepository.findByIdWithChatRoomsMembersJoins(chatRoomId).get();
        Member findMember = memberRepository.findByIdWithChatRoomsMembersJoins(member.getId()).get();
        assertThat(findChatRoom.getChatRoomsMembersJoins().size()).isEqualTo(0);
        assertThat(findMember.getChatRoomsMembersJoins().size()).isEqualTo(0);
        System.out.println(findMember.getChatRoomsMembersJoins());
    }

    @Test
    void deleteChatRoomMembersJoin2() {
        Member member = new Member("test", "test@com", "test url", "local");
        memberRepository.save(member);
        Long chatRoomId = chatService.createChatRoom();

        chatService.addChatMembers(chatRoomId, List.of(member.getId()));

        em.flush();
        em.clear();

        chatService.leaveChatRoom(chatRoomId, member.getId());
        em.flush();
        em.clear();

        Member findMember = memberRepository.findByIdWithChatRoomsMembersJoins(member.getId()).get();
        assertThat(findMember.getChatRoomsMembersJoins().size()).isEqualTo(0);
        ChatRoom chatRoom = chatRoomRepository.findByIdWithChatRoomsMembersJoins(chatRoomId).get();
        assertThat(chatRoom.getChatRoomsMembersJoins().size()).isEqualTo(0);
    }
}