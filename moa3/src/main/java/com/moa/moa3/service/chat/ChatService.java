package com.moa.moa3.service.chat;

import com.moa.moa3.dto.chat.MessageDto;
import com.moa.moa3.entity.chat.ChatRoom;
import com.moa.moa3.entity.chat.ChatRoomsMembersJoin;
import com.moa.moa3.entity.chat.Message;
import com.moa.moa3.entity.member.Member;
import com.moa.moa3.repository.chat.ChatRoomRepository;
import com.moa.moa3.repository.chat.ChatRoomsMembersJoinRepository;
import com.moa.moa3.repository.chat.MessageRepository;
import com.moa.moa3.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomsMembersJoinRepository chatRoomsMembersJoinRepository;
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createChatRoom() {
        ChatRoom chatRoom = new ChatRoom();
        chatRoomRepository.save(chatRoom);
        return chatRoom.getId();
    }

    @Transactional
    public void joinChatRoom(Long chatRoomId, Long memberId) {
        ChatRoom chatRoom = chatRoomRepository.findByIdWithChatRoomsMembersJoins(chatRoomId).orElseThrow(
                () -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다.")
        );
        Member member = memberRepository.findByIdWithChatRoomsMembersJoins(memberId).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지 않습니다.")
        );
        ChatRoomsMembersJoin chatRoomsMembersJoin = new ChatRoomsMembersJoin(chatRoom, member);
        chatRoomsMembersJoinRepository.save(chatRoomsMembersJoin);
        chatRoom.getChatRoomsMembersJoins().add(chatRoomsMembersJoin);
        chatRoom.plusMemberCount();
        member.getChatRoomsMembersJoins().add(chatRoomsMembersJoin);
    }

    @Transactional
    public void leaveChatRoom(Long chatRoomId, Long memberId) {
        ChatRoom chatRoom = chatRoomRepository.findByIdWithChatRoomsMembersJoins(chatRoomId).orElseThrow(
                () -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다.")
        );
        Member member = memberRepository.findByIdWithChatRoomsMembersJoins(memberId).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지 않습니다.")
        );
        chatRoom.getChatRoomsMembersJoins().
                removeIf(chatRoomsMembersJoin -> chatRoomsMembersJoin.getMember().getId().equals(memberId));
        chatRoom.minusMemberCount();
        if (chatRoom.getMemberCount() == 0) {
            chatRoomRepository.delete(chatRoom);
        }
        member.getChatRoomsMembersJoins().
                removeIf(chatRoomsMembersJoin -> chatRoomsMembersJoin.getChatRoom().getId().equals(chatRoomId));
    }

    @Transactional
    public void sendMessage(Long chatRoomId, MessageDto messageDto) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(
                () -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다.")
        );
        Message message = new Message(messageDto);
        messageRepository.save(message);

        chatRoom.getMessages().add(message);
    }
}
