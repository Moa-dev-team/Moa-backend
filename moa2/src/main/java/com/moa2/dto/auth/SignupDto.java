package com.moa2.dto.auth;

import lombok.Data;

@Data
public class SignupDto {
    private String email;
    private String password;
    private String nickname;
}
