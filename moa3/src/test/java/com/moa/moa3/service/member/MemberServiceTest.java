package com.moa.moa3.service.member;

import com.moa.moa3.dto.member.MemberListResponse;
import com.moa.moa3.dto.member.ProfileUpdateRequest;
import com.moa.moa3.entity.member.Member;
import com.moa.moa3.entity.member.profile.Category;
import com.moa.moa3.repository.member.MemberRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;
    @Test
    void 페이징_조회() {
        for (int i=1;i<=10;i++) {
            memberRepository.save(new Member("member"+i, "test email "+i, "test url", "test"));
        }

        MemberListResponse memberList = memberService.getMemberList(null, 5);
        assertThat(memberList.getMembers()).extracting("name").containsExactly("member10", "member9", "member8", "member7", "member6");

        String nextCursor = memberList.getMembers().get(memberList.getMembers().size() - 1).getUpdatedAt().toString();
        MemberListResponse nextMemberList = memberService.getMemberList(nextCursor, 5);
        assertThat(nextMemberList.getMembers()).extracting("name").containsExactly("member5", "member4", "member3", "member2", "member1");
    }

    @Test
    void 조회_도중_업데이트() {
        for (int i=1;i<=10;i++) {
            memberRepository.save(new Member("member"+i, "test email "+i, "test url", "test"));
        }
        MemberListResponse memberList = memberService.getMemberList(null, 5);
        assertThat(memberList.getMembers()).extracting("name").containsExactly("member10", "member9", "member8", "member7", "member6");


        Member member1 = memberRepository.findByName("member1");
        LocalDateTime before = member1.getUpdatedAt();
        member1.setFirstLogin(false);
        em.flush();
        em.clear();
        assertThat(member1.getUpdatedAt()).isNotEqualTo(before);

        String nextCursor = memberList.getMembers().get(memberList.getMembers().size() - 1).getUpdatedAt().toString();

        MemberListResponse nextMemberList = memberService.getMemberList(nextCursor, 5);
        assertThat(nextMemberList.getMembers()).extracting("name").containsExactly("member5", "member4", "member3", "member2");
    }

    @Test
    void 맴버_프로빌_변경() {
        Member member = new Member("member1", "test email 1", "test url", "test");
        memberRepository.save(member);

        em.flush();
        em.clear();
        Member findMember = memberRepository.findByIdWithProfile(member.getId()).get();

        ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest();
        List<String> skills = List.of("Java", "Spring");
        profileUpdateRequest.setSkills(skills);

        memberService.updateMemberProfile(findMember.getId(), profileUpdateRequest);

        em.flush();
        em.clear();

        Member findMember2 = memberRepository.findByIdWithProfile(member.getId()).get();
        assertThat(findMember2.getProfile().getSkills().get(0).getSkill()).isEqualTo(Category.JAVA);
        assertThat(findMember2.getProfile().getSkills().get(1).getSkill()).isEqualTo(Category.SPRING);
    }
}