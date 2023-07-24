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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    MemberRepository memberRepository;

    /**
     * 테스트 용으로 만든 api 입니다. 곧 삭제될 예정입니다.
     * @return
     */
    @GetMapping("")
    public ResponseEntity getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberDetails principal = (MemberDetails) authentication.getPrincipal();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(principal.getMember().getEmail());
    }
}
