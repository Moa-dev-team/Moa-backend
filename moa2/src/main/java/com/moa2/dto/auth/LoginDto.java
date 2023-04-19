package com.moa2.dto.auth;

import lombok.Data;

@Data
public class LoginDto {
    private String email;
    private String password;
}
