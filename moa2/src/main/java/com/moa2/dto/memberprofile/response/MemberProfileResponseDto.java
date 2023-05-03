package com.moa2.dto.memberprofile.response;

import com.moa2.domain.member.Member;
import com.moa2.domain.memberprofile.url.Url;
import lombok.Data;

import java.util.List;

@Data
public class MemberProfileResponseDto {
    private String nickname;
    private String imageUrl;
    private String statusMessage;
    private List<String> urlLinks;
    private List<String> socialLinks;
    private List<String> jobs;
    private List<String> interestTechs;
    private List<String> techStacks;

    public MemberProfileResponseDto(Member member) {
        nickname = member.getNickname();
        imageUrl = member.getImageUrl();
        statusMessage = member.getMemberProfile().getStatusMessage();
        urlLinks = member.getMemberProfile().getUrlLinks().stream()
                .map(Url::getUrl).toList();
        socialLinks = member.getMemberProfile().getSocialLinks().stream()
                .map(Url::getUrl).toList();
        jobs = member.getMemberProfile().getProfileJobs().stream()
                .map(profileJob -> profileJob.getCategory().getName()).toList();
        interestTechs = member.getMemberProfile().getProfileInterestTechs().stream()
                .map(profileInterestTech -> profileInterestTech.getCategory().getName()).toList();
        techStacks = member.getMemberProfile().getProfileTechStacks().stream()
                .map(profileTechStack -> profileTechStack.getCategory().getName()).toList();
    }
}
