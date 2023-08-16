package com.moa.moa3.repository.member;

import com.moa.moa3.dto.member.MemberProfile;
import com.moa.moa3.entity.member.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepositoryQuerydsl {
    // test 할 때만 사용되는 메소드 입니다. 향후 제거할 예정입니다.
    Member findByName(String name);
    Optional<Member> findByEmailWithAuthorities(String email);

    List<MemberProfile> getMembersAfterCursor(String cursor, int limit);
}
