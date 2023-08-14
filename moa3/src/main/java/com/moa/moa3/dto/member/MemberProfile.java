package com.moa.moa3.dto.member;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

/**
 * 모든 사용자에게 보여지는 프로필 정보 DTO 입니다.
 */
@Data
public class MemberProfile {
    private String name;
    private String email;
    private String imageUrl;

    @QueryProjection
    public MemberProfile(String name, String email, String imageUrl) {
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
    }
}
