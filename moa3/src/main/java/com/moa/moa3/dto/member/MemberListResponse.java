package com.moa.moa3.dto.member;

import lombok.Data;

import java.util.List;

@Data
public class MemberListResponse {
    String cursor;
    List<MemberProfile> members;

    public MemberListResponse(String cursor, List<MemberProfile> members) {
        this.cursor = cursor;
        this.members = members;
    }
}
