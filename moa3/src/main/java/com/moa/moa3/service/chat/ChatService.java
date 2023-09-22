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

import java.util.List;

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


    // 채팅방에 소속되지 않은 멤버의 경우 예외가 발생될 수 있게 수정해야합니다.
    @Transactional
    public void addChatMembers(Long chatRoomId, List<Long> memberIds) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(
                () -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다.")
        );
        for (Long memberId : memberIds) {
            Member member = memberRepository.findById(memberId).orElseThrow(
                    () -> new IllegalArgumentException("해당 회원이 존재하지 않습니다.")
            );
            ChatRoomsMembersJoin chatRoomsMembersJoin = new ChatRoomsMembersJoin(chatRoom, member);
            chatRoomsMembersJoinRepository.save(chatRoomsMembersJoin);
            chatRoom.getChatRoomsMembersJoins().add(chatRoomsMembersJoin);
            member.getChatRoomsMembersJoins().add(chatRoomsMembersJoin);
        }
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
        member.getChatRoomsMembersJoins().
                removeIf(chatRoomsMembersJoin -> chatRoomsMembersJoin.getChatRoom().getId().equals(chatRoomId));
    }

    /**
     * 채팅방을 삭제하면 맴버에서 연결된 채팅방도 삭제됩니다.<br>
     * 하지만 맴버가 영속성 컨텍스트에 존재할 경우 맴버에서 연결된 채팅방은 삭제되지 않은 것처럼 보일 수 있습니다.<br>
     * 채팅방을 삭제하면 채팅방에 있는 모든 메세지가 삭제됩니다. <br>
     * @param chatRoomId
     */
    @Transactional
    public void deleteChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(
                () -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다.")
        );
        chatRoomRepository.delete(chatRoom);
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
