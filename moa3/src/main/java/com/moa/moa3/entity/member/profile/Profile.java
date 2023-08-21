package com.moa.moa3.entity.member.profile;

import com.moa.moa3.dto.member.ProfileModifyRequest;
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

    public void update(ProfileModifyRequest profileModifyRequest) {
        this.skills.clear();
        for (String skill : profileModifyRequest.getSkills()) {
            this.skills.add(new ProfileSkill(Category.of(skill)));
        }
    }
}
