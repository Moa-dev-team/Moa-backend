package com.moa.moa3.entity.member.profile;

import com.moa.moa3.dto.member.ProfileUpdateRequest;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Profile {
    @Id @GeneratedValue
    @Column(name = "profile_id")
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileSkill> skills = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private ProfileJob job = new ProfileJob(Category.STUDENT);

    @Column(columnDefinition = "TEXT")
    private String introduction;

    public void update(ProfileUpdateRequest profileUpdateRequest) {
        this.skills.clear();
        for (String skill : profileUpdateRequest.getSkills()) {
            this.skills.add(new ProfileSkill(Category.of(skill)));
        }
    }
}
