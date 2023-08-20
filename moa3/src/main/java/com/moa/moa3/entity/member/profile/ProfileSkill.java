package com.moa.moa3.entity.member.profile;

import jakarta.persistence.*;

/**
 * Profile 과 Skill 는 N:N 관계입니다. <br>
 * ProfileSkill 는 그 중간 테이블입니다.
 */
@Entity
public class ProfileSkill {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category skill;
}
