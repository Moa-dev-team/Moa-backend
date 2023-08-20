package com.moa.moa3.dto.member;

import lombok.Data;

import java.util.List;

@Data
public class ProfileModifyRequest {
    private List<String> skills;
}
