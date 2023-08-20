package com.moa.moa3.entity.member.profile;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * Profile 과 Category 는 N:N 관계입니다. <br>
 * ProfileCategory 는 그 중간 테이블입니다.
 */
@Entity
public class ProfileCategory {
    @Id @GeneratedValue
    private Long id;
}
