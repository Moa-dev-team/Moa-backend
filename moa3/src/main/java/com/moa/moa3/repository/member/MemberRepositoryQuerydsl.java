package com.moa.moa3.repository.member;

import com.moa.moa3.entity.member.Member;

public interface MemberRepositoryQuerydsl {
    Member findByName(String name);
    Member findByEmailWithAuthorities(String email);
}
