package com.moa.moa3.controller.user;

import com.moa.moa3.dto.member.ProfileUpdateRequest;
import com.moa.moa3.security.MemberDetails;
import com.moa.moa3.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * /user/** 패턴을 따르는 api 는 모두 적절한 access token 으로 인증되어 authentication 객체가 생성되었음이 보장됩니다.<br>
 * 따라서 authentication 을 간편하게 불러와서 인증 사용자에 접근할 수 있습니다.
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final MemberService memberService;

    @GetMapping("/profile")
    public ResponseEntity getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberService.getMemberProfile(memberDetails.getMemberId()));
    }

    @PostMapping("/profile/update")
    public ResponseEntity modifyProfile(@RequestBody ProfileUpdateRequest profileUpdateRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        memberService.updateMemberProfile(memberDetails.getMemberId(), profileUpdateRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("update success");
    }
}
