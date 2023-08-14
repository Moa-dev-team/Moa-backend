package com.moa.moa3.service.member;

import com.moa.moa3.dto.member.MemberListResponse;
import com.moa.moa3.entity.member.Member;
import com.moa.moa3.repository.member.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Test
    void 페이징_조회() {
        for (int i=1;i<=10;i++) {
            memberRepository.save(new Member("member"+i, "test email "+i, "test url", "test"));
        }

        MemberListResponse memberList = memberService.getMemberList(null, 5);
        assertThat(memberList.getMembers()).extracting("name").containsExactly("member10", "member9", "member8", "member7", "member6");

        MemberListResponse nextMemberList = memberService.getMemberList(memberList.getCursor(), 5);
        assertThat(nextMemberList.getMembers()).extracting("name").containsExactly("member5", "member4", "member3", "member2", "member1");
    }

    @Test
    void 조회_도중_업데이트() {
        for (int i=1;i<=10;i++) {
            memberRepository.save(new Member("member"+i, "test email "+i, "test url", "test"));
        }
        MemberListResponse memberList = memberService.getMemberList(null, 5);
        assertThat(memberList.getMembers()).extracting("name").containsExactly("member10", "member9", "member8", "member7", "member6");

        memberRepository.findById(1L).ifPresent(member -> {
            member.setFirstLogin(false);
            memberRepository.save(member);
        });

        MemberListResponse nextMemberList = memberService.getMemberList(memberList.getCursor(), 5);
        assertThat(nextMemberList.getMembers()).extracting("name").containsExactly("member5", "member4", "member3", "member2");
    }
}