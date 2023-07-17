package com.moa.moa3.controller.auth;

import com.moa.moa3.dto.oauth.UserProfile;
import com.moa.moa3.service.oauth.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final OAuthService oauthService;

    @GetMapping("login/{provider}")
    public ResponseEntity oauthLogin(@PathVariable String provider, @RequestParam String code) {
        UserProfile userProfile = oauthService.getUserProfile(provider, code);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userProfile);
    }
}
