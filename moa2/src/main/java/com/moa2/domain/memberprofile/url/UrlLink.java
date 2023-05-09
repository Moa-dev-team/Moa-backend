package com.moa2.domain.memberprofile.url;

import jakarta.persistence.Entity;

@Entity
public class UrlLink extends Url{
    public UrlLink() {
    }
    public UrlLink(String url) {
        super(url);
    }
}
