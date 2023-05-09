package com.moa2.domain.memberprofile;

import com.moa2.domain.memberprofile.profilecategory.ProfileInterestTech;
import com.moa2.domain.memberprofile.profilecategory.ProfileJob;
import com.moa2.domain.memberprofile.profilecategory.ProfileTechStack;
import com.moa2.domain.memberprofile.url.SocialLink;
import com.moa2.domain.memberprofile.url.UrlLink;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class MemberProfile {
    @Id @GeneratedValue
    private Long id;

    private String statusMessage;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UrlLink> urlLinks = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SocialLink> socialLinks = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileJob> profileJobs = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileInterestTech> profileInterestTechs = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileTechStack> profileTechStacks = new ArrayList<>();

    public void setProfileJobs(List<ProfileJob> profileJobs) {
        this.profileJobs = profileJobs;
        for (ProfileJob profileJob : profileJobs) {
            profileJob.setMemberProfile(this);
        }
    }
    public void setProfileInterestTechs(List<ProfileInterestTech> profileInterestTechs) {
        this.profileInterestTechs = profileInterestTechs;
        for (ProfileInterestTech profileInterestTech : profileInterestTechs) {
            profileInterestTech.setMemberProfile(this);
        }
    }
    public void setProfileTechStacks(List<ProfileTechStack> profileTechStacks) {
        this.profileTechStacks = profileTechStacks;
        for (ProfileTechStack profileTechStack : profileTechStacks) {
            profileTechStack.setMemberProfile(this);
        }
    }
}
