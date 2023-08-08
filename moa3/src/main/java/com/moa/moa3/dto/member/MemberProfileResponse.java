package com.moa.moa3.dto.member;

import com.moa.moa3.entity.member.Member;
import lombok.Data;

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
