package com.moa.moa3.oauth;

import com.moa.moa3.util.oauth.OAuthProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OAuthPropertiesTest {
    @Autowired
    private OAuthProperties oAuthProperties;

    @Test
    public void OAuthProperties_테스트() {
        System.out.println(oAuthProperties.getUser().getGithub().getClientId());
        System.out.println(oAuthProperties.getUser().getGithub().getClientSecret());
        System.out.println(oAuthProperties.getUser().getGithub().getRedirectUri());
        System.out.println(oAuthProperties.getProvider().getGithub().getTokenUri());
        System.out.println(oAuthProperties.getProvider().getGithub().getUserInfoUri());
    }
}
