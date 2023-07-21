package com.moa.moa3.oauth;

import com.moa.moa3.util.oauth.OAuthProvider;
import com.moa.moa3.exception.oauth.NotFoundProviderException;
import com.moa.moa3.util.oauth.OAuthProviderFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OAuthProviderTest {
    @Autowired
    OAuthProviderFactory oAuthProviderFactory;
    @Test
    public void OAuthProvider_test() {
        OAuthProvider github = oAuthProviderFactory.getProvider("github");
        System.out.println(github.getClientId());
        System.out.println(github.getClientSecret());
        System.out.println(github.getRedirectUri());
        System.out.println(github.getTokenUri());
        System.out.println(github.getUserInfoUri());
    }

    @Test
    public void NotFoundProviderException_test() {
        Assertions.assertThrows(NotFoundProviderException.class, () -> {
            OAuthProvider lorem = oAuthProviderFactory.getProvider("lorem");
        });
    }
}
