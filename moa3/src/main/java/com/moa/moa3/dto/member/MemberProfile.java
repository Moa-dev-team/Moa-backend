package com.moa.moa3.dto.member;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 모든 사용자에게 보여지는 프로필 정보 DTO 입니다.
 */
@Data
public class MemberProfile {
    private String name;
    private String email;
    private String imageUrl;
    private LocalDateTime updatedAt;

    @QueryProjection
    public MemberProfile(String name, String email, String imageUrl, LocalDateTime updatedAt) {
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.updatedAt = updatedAt;
    }
}
