package com.moa.moa3.dto.member;

import com.moa.moa3.entity.member.Member;
import com.moa.moa3.entity.member.profile.Category;
import com.moa.moa3.entity.member.profile.ProfileSkill;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 모든 사용자에게 보여지는 프로필 정보 DTO 입니다.
 */
@Data
public class MemberProfile {
    private Long memberId;
    private String name;
    private String email;
    private String imageUrl;
    private LocalDateTime updatedAt;
    private List<Category> skills;
    private String job;

    public MemberProfile(Member member) {
        memberId = member.getId();
        name = member.getName();
        email = member.getEmail();
        imageUrl = member.getImageUrl();
        updatedAt = member.getUpdatedAt();
        skills = member.getProfile().getSkills().stream().map(ProfileSkill::getSkill).toList();
        job = member.getProfile().getJob().getName();
    }
}
