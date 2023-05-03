package com.moa2.dto.memberprofile.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdateProfileResponseDto {
    private String nickname;
    private String imageUrl;
    private String statusMessage;
    private List<String> UrlLinks;
    private List<String> SocialLinks;
    private List<String> jobs;
    private List<String> interestTechs;
    private List<String> techStacks;
}
