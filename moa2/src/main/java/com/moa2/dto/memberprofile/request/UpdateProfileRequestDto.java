package com.moa2.dto.memberprofile.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdateProfileRequestDto {
    private String nickname;
    private String imageUrl;
    private String statusMessage;
    private List<String> urlLinks;
    private List<String> socialLinks;
    private List<String> jobs;
    private List<String> interestTechs;
    private List<String> techStacks;
}
