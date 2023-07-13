package com.moa.moa3.repository.member;

import com.moa.moa3.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
