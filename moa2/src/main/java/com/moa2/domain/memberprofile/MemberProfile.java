package com.moa2.domain.memberprofile;

import com.moa2.domain.memberprofile.profilecategory.ProfileInterestTech;
import com.moa2.domain.memberprofile.profilecategory.ProfileJob;
import com.moa2.domain.memberprofile.profilecategory.ProfileTechStack;
import com.moa2.domain.memberprofile.url.SocialLink;
import com.moa2.domain.memberprofile.url.UrlLink;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

    @OneToMany
    private List<UrlLink> urlLinks = new ArrayList<>();
    @OneToMany
    private List<SocialLink> socialLinks = new ArrayList<>();

    @OneToMany(mappedBy = "memberProfile")
    private List<ProfileJob> profileJobs = new ArrayList<>();
    @OneToMany(mappedBy = "memberProfile")
    private List<ProfileInterestTech> profileInterestTechs = new ArrayList<>();
    @OneToMany(mappedBy = "memberProfile")
    private List<ProfileTechStack> profileTechStacks = new ArrayList<>();
}
