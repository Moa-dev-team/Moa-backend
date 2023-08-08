package com.moa.moa3.controller.user;

import com.moa.moa3.repository.member.MemberRepository;
import com.moa.moa3.security.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/profile")
    public ResponseEntity getProfile(@RequestHeader("Authorization") String accessTokenInHeader) {

    }

    private String parseToken(String accessTokenInHeader) {
        return accessTokenInHeader.substring(7);
    }
}
