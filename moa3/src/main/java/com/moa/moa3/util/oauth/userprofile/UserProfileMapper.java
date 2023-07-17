package com.moa.moa3.util.oauth.userprofile;

import com.moa.moa3.dto.oauth.UserProfile;

import java.util.Map;

// mapper 가 추가된다면 아래 interface 를 implements 해주면 됩니다.
public interface UserProfileMapper {
    UserProfile map(Map<String, Object> attributes);
}
