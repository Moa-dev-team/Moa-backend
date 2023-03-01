package com.moa.sercurity.data;

import lombok.Getter;

@Getter
public enum JwtType {
  ACCESS_TOKEN("accessToken", 60 * 60 * 1000L),
  REFRESH_TOKEN("refreshToken", 7 * 24 * 60 * 60 * 1000L),
  ;

  private String name;
  private Long expiredMilliSeconds;

  JwtType(String name, Long expiredMilliSeconds) {
    this.name = name;
    this.expiredMilliSeconds = expiredMilliSeconds;
  }
}
