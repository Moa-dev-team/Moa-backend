package com.moa2.domain.memberprofile;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Url {
    @Id @GeneratedValue
    private Long id;

    private String url;
}
