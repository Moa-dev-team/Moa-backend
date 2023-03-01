package com.moa.domain.user.entity;

import com.moa.sercurity.dto.SignupDto;
import javax.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "email")
  private String email;

  @Column(name = "name")
  private String name;

  @Column(name = "password")
  private String password;

  @Column(name = "profile")
  private String profile;

  @Enumerated(EnumType.STRING)
  private Role role;

  //==생성 메서드==//
  public static User registerUser(SignupDto signupDto) {
    User user = new User();
    user.email = signupDto.getEmail();
    user.password = signupDto.getPassword();
    user.role = Role.USER;

    return user;
  }
}
