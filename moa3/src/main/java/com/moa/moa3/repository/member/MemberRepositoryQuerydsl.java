package com.moa.moa3.repository.member;

import com.moa.moa3.entity.Member;

public interface MemberRepositoryQuerydsl {
    Member findByName(String name);
}
