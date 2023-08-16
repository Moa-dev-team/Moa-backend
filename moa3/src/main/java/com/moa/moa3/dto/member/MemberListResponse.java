package com.moa.moa3.dto.member;

import lombok.Data;

import java.util.List;

@Data
public class MemberListResponse {
    List<MemberProfile> members;
    public MemberListResponse(List<MemberProfile> members) {
        this.members = members;
    }
}
