package com.moa.moa3.repository.member;

import com.moa.moa3.dto.global.MembersRequestCondition;
import com.moa.moa3.dto.member.MemberProfile;
import com.moa.moa3.entity.member.Member;
import com.moa.moa3.entity.member.profile.Category;
import com.moa.moa3.entity.member.profile.Profile;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.moa.moa3.entity.member.QMember.*;
import static com.moa.moa3.entity.member.profile.QProfile.profile;
import static com.moa.moa3.entity.member.profile.QProfileSkill.profileSkill;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberRepositoryQuerydslImpl implements MemberRepositoryQuerydsl{

    private final JPAQueryFactory query;
    private final EntityManager em;

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
                        .fetchOne()
        );
    }

    @Override
    public List<MemberProfile> getMembersAfterCursor(String cursor, int limit) {
        BooleanBuilder whereClause = new BooleanBuilder();
        if (cursor != null) {
            LocalDateTime cursorDateTime = LocalDateTime.parse(cursor);
            whereClause.and(member.updatedAt.lt(cursorDateTime));
        }

        List<Member> result = query
                .select(member)
                .from(member)
                .join(member.profile, profile)
                .where(whereClause)
                .orderBy(member.updatedAt.desc())
                .limit(limit)
                .fetch();
        return result.stream().map(MemberProfile::new).toList();
    }

    @Override
    public List<MemberProfile> getMembersAfterCursor(String cursor, int limit, MembersRequestCondition condition) {
        if (condition == null || condition.getCategories() == null || condition.getCategories().isEmpty()) {
            return getMembersAfterCursor(cursor, limit);
        }
        List<Category> categories = condition.getCategories();
        // 작동은 하지만 categoryPath 부분이 이해가 안됩니다.
        EnumPath<Category> categoryPath = Expressions.enumPath(Category.class, profileSkill, "skill"); // "category" 대신 "skill"로 수정

        BooleanBuilder whereClause = new BooleanBuilder();
        if (cursor != null) {
            LocalDateTime cursorDateTime = LocalDateTime.parse(cursor);
            whereClause.and(member.updatedAt.lt(cursorDateTime));
        }
        JPAQuery<Profile> subQuery = new JPAQuery<>(em)
                .select(profile)
                .from(profile)
                .join(profile.skills, profileSkill)
                .where(profileSkill.skill.in(categories))
                .groupBy(profile.id)
                .having(categoryPath.count().goe((long) categories.size()));

        List<Member> result = query
                .selectFrom(member)
                .join(member.profile, profile)
                .where(profile.in(subQuery.fetch()).and(whereClause))
                .orderBy(member.updatedAt.desc())
                .limit(limit)
                .fetch();

        return result.stream().map(MemberProfile::new).toList();
    }


    @Override
    public Optional<Member> findByIdWithProfile(Long id) {
        return Optional.ofNullable(
                query
                        .selectFrom(member)
                        .join(member.profile)
                        .fetchJoin()
                        .where(member.id.eq(id))
                        .fetchFirst()
        );
    }

    @Override
    public Optional<Member> findByIdWithChatRoomsMembersJoins(Long id) {
        return Optional.ofNullable(
                query
                        .select(member)
                        .leftJoin(member.chatRoomsMembersJoins).fetchJoin()
                        .where(member.id.eq(id))
                        .fetchOne()
        );
    }
}
