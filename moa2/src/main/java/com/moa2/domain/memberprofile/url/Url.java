package com.moa2.domain.memberprofile.url;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Url {
    @Id @GeneratedValue
    private Long id;

    private String url;

    public Url() {
    }

    public Url(String url) {
        this.url = url;
    }
}
