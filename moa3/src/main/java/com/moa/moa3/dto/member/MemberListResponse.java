package com.moa.moa3.dto.member;

import lombok.Data;

import java.util.List;

@Data
public class MemberListResponse {
    List<MemberProfile> members;
    String nextCursor;
    boolean nextPage = true;

    public MemberListResponse(List<MemberProfile> members,String nextCursor, boolean nextPage) {
        this.members = members;
        this.nextCursor = nextCursor;
        this.nextPage = nextPage;
    }
}
