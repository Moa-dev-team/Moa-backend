package com.moa.moa3.dto.member;

import lombok.Data;

import java.util.List;

@Data
public class ProfileUpdateRequest {
    private String name;
    private String introduction;
    private String job;
    private List<String> skills;
}
