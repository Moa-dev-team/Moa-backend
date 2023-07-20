package com.moa.moa3.repository.member;

import com.moa.moa3.entity.member.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import static com.moa.moa3.entity.member.QMember.*;

@Transactional(readOnly = true)
abstract class MemberRepositoryQuerydslImpl implements MemberRepositoryQuerydsl{
    @PersistenceContext
    EntityManager em;

    @Override
    public Member findByName(String name) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        return query
                .selectFrom(member)
                .where(member.name.eq(name))
                .fetchOne();
    }

    @Override
    public Member findByEmailWithAuthorities(String email) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        return query
                .selectFrom(member)
                .join(member.authorities)
                .fetchJoin()
                .where(member.email.eq(email))
                .fetchOne();
    }
}
