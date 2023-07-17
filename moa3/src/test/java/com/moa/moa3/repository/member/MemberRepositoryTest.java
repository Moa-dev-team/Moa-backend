package com.moa.moa3.repository.member;

import com.moa.moa3.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void testQuery() {
        Member member = new Member("test", "test@com", "test.com");
        memberRepository.save(member);

        //초기화
        em.flush();
        em.clear();

        Member findMember = memberRepository.findByName("test");

        assertThat(member.getName()).isEqualTo(findMember.getName());
    }
}