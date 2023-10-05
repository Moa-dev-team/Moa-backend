package com.moa.moa3.service.chat;

import com.moa.moa3.dto.chat.CreateChatRequest;
import com.moa.moa3.dto.chat.MessageDto;
import com.moa.moa3.dto.chat.MessageType;
import com.moa.moa3.entity.chat.ChatRoom;
import com.moa.moa3.entity.chat.ChatRoomsMembersJoin;
import com.moa.moa3.entity.member.Member;
import com.moa.moa3.repository.chat.ChatRoomRepository;
import com.moa.moa3.repository.chat.ChatRoomsMembersJoinRepository;
import com.moa.moa3.repository.member.MemberRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
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

    private final static CreateChatRequest REQUEST = new CreateChatRequest(
            new ArrayList<>(),
            "test"
    );

    @Autowired
    EntityManager em;

    @Test
    void joinChatRoomTest() {
        Member member = new Member("test", "test@com", "test url", "local");
        memberRepository.save(member);

        Long chatRoomId = chatService.createChatRoom(REQUEST);
        chatService.addChatMembers(chatRoomId, List.of(member.getId()));
//
        Member findMember = memberRepository.findByIdWithChatRooms(member.getId()).get();
        assertThat(findMember.getChatRoomsMembersJoins().size()).isEqualTo(1);
        assertThat(findMember.getChatRoomsMembersJoins().get(0).getChatRoom().getTitle()).isEqualTo("test");

        ChatRoom findChatRoom = chatRoomRepository.findByIdWithMembers(chatRoomId).get();
        assertThat(findChatRoom.getChatRoomsMembersJoins().get(0).getMember().getName()).isEqualTo("test");
    }

    @Test
    void sendMessageTest() {
        Long chatRoomId = chatService.createChatRoom(REQUEST);
        MessageDto messageDto = new MessageDto(MessageType.JOIN, "나 등장", 1L, chatRoomId);
        chatService.sendMessage(chatRoomId, messageDto);

        ChatRoom findChatRoom = chatRoomRepository.findByIdWithMessages(chatRoomId).get();
        assertThat(findChatRoom.getMessages().size()).isEqualTo(1);
    }

    @Test
    void leaveChatTest() {
        Member member = new Member("test", "test@com", "test url", "local");
        memberRepository.save(member);

        Long chatRoomId = chatService.createChatRoom(REQUEST);
        chatService.addChatMembers(chatRoomId, List.of(member.getId()));

        em.flush();
        em.clear();

        Member findMember = memberRepository.findByIdWithChatRooms(member.getId()).get();
        findMember.getChatRoomsMembersJoins().removeIf(
                chatRoomsMembersJoin -> chatRoomsMembersJoin.getChatRoom().getId().equals(chatRoomId)
        );
        ChatRoom findChatRoom = chatRoomRepository.findByIdWithMembers(chatRoomId).get();
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

        Long chatRoomId = chatService.createChatRoom(REQUEST);
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

        ChatRoom findChatRoom = chatRoomRepository.findByIdWithMembers(chatRoomId).get();
        Member findMember = memberRepository.findByIdWithChatRooms(member.getId()).get();
        assertThat(findChatRoom.getChatRoomsMembersJoins().size()).isEqualTo(0);
        assertThat(findMember.getChatRoomsMembersJoins().size()).isEqualTo(0);
        System.out.println(findMember.getChatRoomsMembersJoins());
    }

    @Test
    void deleteChatRoomMembersJoin2() {
        Member member = new Member("test", "test@com", "test url", "local");
        memberRepository.save(member);
        Long chatRoomId = chatService.createChatRoom(REQUEST);

        chatService.addChatMembers(chatRoomId, List.of(member.getId()));

        em.flush();
        em.clear();

        chatService.leaveChatRoom(chatRoomId, member.getId());
        em.flush();
        em.clear();

        Member findMember = memberRepository.findByIdWithChatRooms(member.getId()).get();
        assertThat(findMember.getChatRoomsMembersJoins().size()).isEqualTo(0);
        ChatRoom chatRoom = chatRoomRepository.findByIdWithMembers(chatRoomId).get();
        assertThat(chatRoom.getChatRoomsMembersJoins().size()).isEqualTo(0);
    }
}