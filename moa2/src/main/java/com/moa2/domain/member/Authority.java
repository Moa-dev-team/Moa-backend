package com.moa2.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Authority {
    @Id
    @GeneratedValue
    @Column(name = "authority_id")
    private Long id;
    private String name;

    public Authority(String role) {
        this.name = role;
    }

    public Authority() {

    }
}

