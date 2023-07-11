package com.moa.moa3.dto.oauth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserData {
    private String name;
    private String email;
    private String imageUrl;
}
