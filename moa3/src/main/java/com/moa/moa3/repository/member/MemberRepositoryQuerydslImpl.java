package com.moa.moa3.repository.member;

import com.moa.moa3.dto.member.MemberProfile;
import com.moa.moa3.dto.member.QMemberProfile;
import com.moa.moa3.entity.member.Member;
import com.mysema.commons.lang.Pair;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
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

    @Override
    public List<MemberProfile> getMembersAfterCursor(String cursor, int limit) {
        if (cursor == null) {
            return query
                    .select(new QMemberProfile(member.name, member.email, member.imageUrl, member.updatedAt))
                    .from(member)
                    .orderBy(member.updatedAt.desc())
                    .limit(limit)
                    .fetch();
        } else {
            LocalDateTime cursorDateTime = LocalDateTime.parse(cursor);
            return query
                    .select(new QMemberProfile(member.name, member.email, member.imageUrl, member.updatedAt))
                    .from(member)
                    .where(member.updatedAt.lt(cursorDateTime))
                    .orderBy(member.updatedAt.desc())
                    .limit(limit)
                    .fetch();
        }
    }

}
