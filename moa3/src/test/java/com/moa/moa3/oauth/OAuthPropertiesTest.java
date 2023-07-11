package com.moa.moa3.oauth;

import com.moa.moa3.dto.oauth.OAuthProvider;
import com.moa.moa3.util.oauth.OAuthProperties;
import com.moa.moa3.util.oauth.OAuthProviderFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OAuthPropertiesTest {
    @Autowired
    private OAuthProperties oAuthProperties;

    @Autowired
    private OAuthProviderFactory oAuthProviderFactory;

    @Test
    public void OAuthProperties_test() {
        System.out.println(oAuthProperties.getGithub().getClientId());
        System.out.println(oAuthProperties.getGithub().getClientSecret());
        System.out.println(oAuthProperties.getGithub().getRedirectUri());
        System.out.println(oAuthProperties.getGithub().getTokenUri());
        System.out.println(oAuthProperties.getGithub().getUserInfoUri());
    }

    @Test
    public void OAuthProvider_test() {
        OAuthProvider github = oAuthProviderFactory.getProvider("github");
        System.out.println(github.getClientId());
        System.out.println(github.getClientSecret());
        System.out.println(github.getRedirectUri());
        System.out.println(github.getTokenUri());
        System.out.println(github.getUserInfoUri());
    }
}
