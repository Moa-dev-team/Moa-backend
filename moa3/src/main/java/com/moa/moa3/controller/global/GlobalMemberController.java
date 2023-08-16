package com.moa.moa3.controller.global;

import com.moa.moa3.security.MemberDetails;
import com.moa.moa3.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/global")
@RequiredArgsConstructor
public class GlobalMemberController {
    private final MemberService memberService;
    @GetMapping("/members")
    public ResponseEntity getMembers(
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "20") int limit
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberService.getMemberList(cursor, limit));
    }
}
