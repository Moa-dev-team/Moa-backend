package com.moa.moa3.repository.member;

import com.moa.moa3.entity.member.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void testQuery() {
        Member member = new Member("test", "test@com", "test.com", "local");
        memberRepository.save(member);

        //초기화
        em.flush();
        em.clear();

        Member findMember = memberRepository.findByName("test");

        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    // fetchJoin 이 정상적으로 작동하는지 테스트
    @Test
    public void findByIdWithProfile() {
        Member member = new Member("member1", "test email 1", "test url", "test");
        memberRepository.save(member);

        em.flush();
        em.clear();
        Member findMember = memberRepository.findByIdWithProfile(member.getId()).get();

        assertThat(Hibernate.isInitialized(findMember.getProfile())).isTrue();
    }
}