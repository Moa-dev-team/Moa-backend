package com.moa2.domain.memberprofile;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class MemberProfile {
    @Id @GeneratedValue
    private Long id;

}
