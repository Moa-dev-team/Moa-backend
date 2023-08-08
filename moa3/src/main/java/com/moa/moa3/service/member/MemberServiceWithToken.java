package com.moa.moa3.service.member;

import com.moa.moa3.validation.jwt.JwtTokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceWithToken {
    private final JwtTokenValidator jwtTokenValidator;

    public Long getMemberId(String token) {
        jwtTokenValidator.validateToken(token);


    }

}
