package com.moa2.controller.member;

import com.moa2.service.auth.AuthService;
import com.moa2.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final AuthService authService;
    private final MemberService memberService;

    @GetMapping("/test-admin")
    public ResponseEntity testAdmin(@RequestHeader("Authorization") String accessTokenInHeader) {
        Long memberId = authService.getMemberId(accessTokenInHeader);
        String memberInfo = memberService.getMemberInfo(memberId);
        return ResponseEntity.ok().body(memberInfo);
    }
}
