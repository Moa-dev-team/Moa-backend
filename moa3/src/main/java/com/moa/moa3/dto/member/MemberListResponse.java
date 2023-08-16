package com.moa.moa3.dto.member;

import lombok.Data;

import java.util.List;

@Data
public class MemberListResponse {
    List<MemberProfile> members;
    String nextCursor;

    public MemberListResponse(List<MemberProfile> members,String nextCursor) {
        this.members = members;
        this.nextCursor = nextCursor;
    }
}
