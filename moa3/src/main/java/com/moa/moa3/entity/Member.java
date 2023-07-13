package com.moa.moa3.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class Member extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String imageUrl;

    public Member(String name, String email, String imageUrl) {
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
    }
}
