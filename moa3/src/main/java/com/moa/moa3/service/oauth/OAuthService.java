package com.moa.moa3.service.oauth;

import com.moa.moa3.dto.oauth.UserData;
import org.springframework.stereotype.Service;

@Service
public class OAuthService {

    public UserData getUserData(String provider, String code) {
        return new UserData("lorem", "ipsum", "dolor");
    }
}
