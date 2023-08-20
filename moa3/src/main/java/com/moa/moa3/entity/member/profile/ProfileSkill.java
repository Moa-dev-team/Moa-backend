package com.moa.moa3.entity.member.profile;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ProfileSkill {
    @Id @GeneratedValue
    @Column(name = "profile_skill_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category skill;
    public ProfileSkill(Category skill) {
        this.skill = skill;
    }
}
