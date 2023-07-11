package com.moa.moa3.validation.oauth;

import com.moa.moa3.dto.oauth.UserProfile;
import com.moa.moa3.exception.oauth.NotFoundEmailException;
import com.moa.moa3.exception.oauth.NotFoundOAuthIdException;

public class UserProfileValidator {

    public static void validate(UserProfile userProfile) throws NotFoundEmailException, NotFoundOAuthIdException {
        if (userProfile.getOAuthId() == null || userProfile.getOAuthId().isEmpty()) {
            throw new NotFoundOAuthIdException();
        }
        if (userProfile.getEmail() == null || userProfile.getEmail().isEmpty()) {
            throw new NotFoundEmailException();
        }
    }
}

