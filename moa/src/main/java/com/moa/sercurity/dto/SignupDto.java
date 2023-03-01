package com.moa.sercurity.dto;

import javax.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupDto {

  @NotEmpty
  private String email;
  @NotEmpty
  private String password;

  public static SignupDto encodePassword(SignupDto signupDto, String encodedPassword) {
    SignupDto signupDtoWithEncodedPassword = new SignupDto();
    signupDtoWithEncodedPassword.email = signupDto.getEmail();
    signupDtoWithEncodedPassword.password = encodedPassword;
    return signupDtoWithEncodedPassword;
  }
}
