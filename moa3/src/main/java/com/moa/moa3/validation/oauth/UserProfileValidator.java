package com.moa.moa3.validation.oauth;

import com.moa.moa3.dto.oauth.UserProfile;
import com.moa.moa3.exception.oauth.IllegalEmailException;
import com.moa.moa3.exception.oauth.IllegalOAuthIdException;

public class UserProfileValidator {

    private UserProfileValidator() {}

    public static void validate(UserProfile userProfile) throws IllegalEmailException, IllegalOAuthIdException {
        if (userProfile.getOAuthId() == null || userProfile.getOAuthId().isEmpty()) {
            throw new IllegalOAuthIdException();
        }
        if (userProfile.getEmail() == null || userProfile.getEmail().isEmpty()) {
            throw new IllegalEmailException();
        }
    }
}

