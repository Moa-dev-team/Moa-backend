package com.moa.moa3.dto.member;

import lombok.Data;

import java.util.List;

@Data
public class ProfileUpdateRequest {
    private List<String> skills;
}
