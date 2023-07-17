package com.moa.moa3.oauth;

import com.moa.moa3.dto.oauth.UserProfile;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OAuthValidationTest {
    @Test
    public void UserProfileValidator_test() {
        // email 이 비어있는 경우
        assertThrows(IllegalArgumentException.class, () -> {
            UserProfile userProfile = new UserProfile(
                    "github", "hosung", "", "123");
        });
        // email 이 null 인 경우

        assertThrows(IllegalArgumentException.class, () -> {
            UserProfile userProfile2 = new UserProfile(
                    "github", "hosung", null, "123");
        });
        // email 이 공백만 포함하고 있는 경우
        assertThrows(IllegalArgumentException.class, () -> {
            UserProfile userProfile3 = new UserProfile(
                    "github", "hosung", "  ", "123");
        });

        // oAuthId 가 비어있는 경우
        assertThrows(IllegalArgumentException.class, () -> {
            UserProfile userProfile4 = new UserProfile(
                    "", "hosung", "aas@asd", "123");
        });
        // oAuthId 가 null 인 경우
        assertThrows(IllegalArgumentException.class, () -> {
            UserProfile userProfile5 = new UserProfile(
                    null, "hosung", "aas@asd", "123");
        });
        // oAuthId 가 공백만 포함하고 있는 경우
        assertThrows(IllegalArgumentException.class, () -> {
            UserProfile userProfile6 = new UserProfile(
                    "  ", "hosung", "aas@asd", "123");
        });
    }
}
