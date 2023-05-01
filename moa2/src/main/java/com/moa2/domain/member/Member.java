package com.moa2.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moa2.security.oauth2.AuthProvider;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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

    private boolean isNew = true;
    //    @Email
    @Column(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    private String imageUrl;

//    @Column(nullable = false)
//    private Boolean emailVerified = false;

//    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;
    private String providerId;


    @ManyToMany
    @JoinTable(name = "member_authority",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private Set<Authority> authorities = new HashSet<>();
}