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
    public void OAuthProperties_test() {
        System.out.println(oAuthProperties.getGithub().getClientId());
        System.out.println(oAuthProperties.getGithub().getClientSecret());
        System.out.println(oAuthProperties.getGithub().getRedirectUri());
        System.out.println(oAuthProperties.getGithub().getTokenUri());
        System.out.println(oAuthProperties.getGithub().getUserInfoUri());
    }

    @Test
    public void googleOAuthPropertiesTest() {
        System.out.println(oAuthProperties.getGoogle().getClientId());
        System.out.println(oAuthProperties.getGoogle().getClientSecret());
        System.out.println(oAuthProperties.getGoogle().getRedirectUri());
        System.out.println(oAuthProperties.getGoogle().getTokenUri());
        System.out.println(oAuthProperties.getGoogle().getUserInfoUri());
    }


}
