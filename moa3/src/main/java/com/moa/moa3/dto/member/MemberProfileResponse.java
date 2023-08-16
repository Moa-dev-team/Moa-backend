package com.moa.moa3.dto.member;

import com.moa.moa3.entity.member.Member;
import lombok.Data;

/**
 * 로그인된 사용자가 자신의 프로필을 조회할 경우 제공되는 응답 DTO 입니다.
 */
@Data
public class MemberProfileResponse {
    private String name;
    private String email;
    private String imageUrl;

    public MemberProfileResponse(Member member) {
        name = member.getName();
        email = member.getEmail();
        imageUrl = member.getImageUrl();
    }
}
