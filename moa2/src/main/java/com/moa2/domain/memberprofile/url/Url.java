package com.moa2.domain.memberprofile.url;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
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
