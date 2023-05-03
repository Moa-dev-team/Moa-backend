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

    @OneToMany(mappedBy = "memberProfile",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileJob> profileJobs = new ArrayList<>();
    @OneToMany(mappedBy = "memberProfile",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileInterestTech> profileInterestTechs = new ArrayList<>();
    @OneToMany(mappedBy = "memberProfile",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileTechStack> profileTechStacks = new ArrayList<>();
}
