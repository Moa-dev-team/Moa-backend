package com.moa2.dto.auth.request;

import lombok.Data;

@Data
public class LoginDto {
    private String email;
    private String password;
}
