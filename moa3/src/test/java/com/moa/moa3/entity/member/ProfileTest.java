package com.moa.moa3.entity.member;

import com.moa.moa3.repository.member.MemberRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProfileTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    public void get_profile_id() {
        Member member = new Member("test", "test email", "test url", "local");
        memberRepository.save(member);
        System.out.println("member.getProfile().getId() = " + member.getProfile().getId());
        em.flush();
        em.clear();

        Member findMember = memberRepository.findById(member.getId()).get();
        assertThat(member.getProfile().getId()).isEqualTo(findMember.getProfile().getId());
    }
}