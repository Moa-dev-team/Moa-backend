package com.moa.moa3.entity.member.profile;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Profile {
    @Id @GeneratedValue
    private Long id;
}
