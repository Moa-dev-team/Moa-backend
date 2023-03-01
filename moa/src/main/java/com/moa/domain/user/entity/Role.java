package com.moa.domain.user.entity;

import lombok.Getter;

@Getter
public enum Role {
  USER("ROLE_USER", "사용자"), ADMIN("ROLE_ADMIN", "관리자"),
  ;
  private String key;
  private String name;

  Role(String key, String name) {
    this.key = key;
    this.name = name;
  }
}
