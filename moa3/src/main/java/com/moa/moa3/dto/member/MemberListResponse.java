package com.moa.moa3.dto.member;

import lombok.Data;

import java.util.List;

@Data
public class MemberListResponse {
    String cursor;
    List<MemberProfileResponse> members;
}
