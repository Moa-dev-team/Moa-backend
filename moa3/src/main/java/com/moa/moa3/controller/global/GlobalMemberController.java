package com.moa.moa3.controller.global;

import com.moa.moa3.dto.global.MembersRequestCondition;
import com.moa.moa3.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/global")
@RequiredArgsConstructor
public class GlobalMemberController {
    private final MemberService memberService;
    @PostMapping("/members")
    public ResponseEntity getMembers(
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "20") int limit,
            @RequestBody(required = false) MembersRequestCondition condition
            ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberService.getMemberList(cursor, limit, condition));
    }
}
