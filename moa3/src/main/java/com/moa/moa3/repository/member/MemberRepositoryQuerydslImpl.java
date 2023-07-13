package com.moa.moa3.repository.member;

import com.moa.moa3.entity.Member;
import com.moa.moa3.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import static com.moa.moa3.entity.QMember.*;

public class MemberRepositoryQuerydslImpl implements MemberRepositoryQuerydsl{
    @PersistenceContext
    EntityManager em;

    JPAQueryFactory query = new JPAQueryFactory(em);

    @Override
    public Member findByName(String name) {
        Member result = query
                .selectFrom(member)
                .where(member.name.eq(name))
                .fetchOne();
        return result;
    }
}
