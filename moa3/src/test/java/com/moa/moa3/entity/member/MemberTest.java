package com.moa.moa3.entity.member;

import com.moa.moa3.dto.member.ProfileUpdateRequest;
import com.moa.moa3.repository.member.MemberRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;
    @Test
    public void memberUpdate() {
        Member member = new Member("test", "test email", "test image", "test provider");
        ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest();
        profileUpdateRequest.setName("test name");
        profileUpdateRequest.setSkills(List.of("Python", "Spring"));
        member.update(profileUpdateRequest);
        memberRepository.save(member);

        em.flush();
        em.clear();

        Member findMember = memberRepository.findById(member.getId()).get();
        assertThat(findMember.getName()).isEqualTo("test name");
        assertThat(findMember.getProfile().getSkills().size()).isEqualTo(2);
    }
}