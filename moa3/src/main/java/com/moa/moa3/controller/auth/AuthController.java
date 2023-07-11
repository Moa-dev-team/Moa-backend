package com.moa.moa3.controller.auth;

import com.moa.moa3.dto.oauth.UserData;
import com.moa.moa3.service.oauth.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final OauthService oauthService;

    @GetMapping("login/{provider}")
    public ResponseEntity oauthLogin(@PathVariable String provider, @RequestParam String code) {
        UserData userData = oauthService.getUserData(provider, code);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userData);
    }
}
