package com.moa2.controller.member;

import com.moa2.dto.memberprofile.request.UpdateProfileRequestDto;
import com.moa2.service.auth.AuthService;
import com.moa2.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final AuthService authService;
    private final MemberService memberService;

    //삭제 예정
    @GetMapping("/mypage")
    public ResponseEntity mypage(@RequestHeader("Authorization") String accessTokenInHeader) {
        Long memberId = authService.getMemberIdInBearerAccessToken(accessTokenInHeader);
        String memberInfo = memberService.getMemberInfo(memberId);
        return ResponseEntity.ok().body(memberInfo);
    }

    @GetMapping("/my-profile")
    public ResponseEntity myProfile(@RequestHeader("Authorization") String accessTokenInHeader) {
        Long memberId = authService.getMemberIdInBearerAccessToken(accessTokenInHeader);
        return ResponseEntity.ok().body(memberService.getMemberProfile(memberId));
    }

    @PostMapping("/update-profile")
    public ResponseEntity updateProfile(@RequestHeader("Authorization") String accessTokenInHeader,
                                        @RequestBody UpdateProfileRequestDto updateProfileRequestDto) {
        Long memberId = authService.getMemberIdInBearerAccessToken(accessTokenInHeader);
        memberService.updateMemberProfile(memberId, updateProfileRequestDto);
        return ResponseEntity.ok().body("update success");
    }
}
