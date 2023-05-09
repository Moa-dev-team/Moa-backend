package com.moa2.domain.memberprofile.url;

import jakarta.persistence.Entity;

@Entity
public class SocialLink extends Url{
    public SocialLink() {
    }
    public SocialLink(String url) {
        super(url);
    }
}
