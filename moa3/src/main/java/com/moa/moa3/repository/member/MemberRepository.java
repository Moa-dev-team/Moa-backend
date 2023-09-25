package com.moa.moa3.repository.member;

import com.moa.moa3.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryQuerydsl {
    Optional<Member> findByEmail(String email);
}
