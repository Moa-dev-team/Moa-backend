package com.moa2.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moa2.domain.memberprofile.MemberProfile;
import com.moa2.security.oauth2.AuthProvider;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

//    @Column(nullable = false)
    private String nickname;

    private boolean isFirstLogin = true;
    //    @Email
    @Column(unique = true)
    private String email;

    // 삭제 예정
    @JsonIgnore
    private String password;

    private String imageUrl;

//    @Column(nullable = false)
//    private Boolean emailVerified = false;

//    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;
    private String providerId;

    @OneToOne
    @JoinColumn(name = "member_profile_id")
    private MemberProfile memberProfile;

    @ManyToMany
    @JoinTable(name = "member_authority",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private Set<Authority> authorities = new HashSet<>();
}