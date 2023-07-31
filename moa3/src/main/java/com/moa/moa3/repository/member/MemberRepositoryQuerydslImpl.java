package com.moa.moa3.repository.member;

import com.moa.moa3.entity.member.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.moa.moa3.entity.member.QMember.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberRepositoryQuerydslImpl implements MemberRepositoryQuerydsl{

    private final JPAQueryFactory query;

    @Override
    public Member findByName(String name) {
        return query
                .selectFrom(member)
                .where(member.name.eq(name))
                .fetchOne();
    }

    @Override
    public Optional<Member> findByEmailWithAuthorities(String email) {
        return Optional.ofNullable(
                query
                        .selectFrom(member)
                        .join(member.authorities)
                        .fetchJoin()
                        .where(member.email.eq(email))
                        .fetchFirst()
        );
    }
}
