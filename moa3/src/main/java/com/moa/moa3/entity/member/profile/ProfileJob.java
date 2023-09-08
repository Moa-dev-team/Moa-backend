package com.moa.moa3.entity.member.profile;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ProfileJob {
    @Id
    @GeneratedValue
    @Column(name = "profile_job_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category job;

    public ProfileJob(Category job) {
        this.job = job;
    }

    public String getName() {
        return job.getName();
    }
}
