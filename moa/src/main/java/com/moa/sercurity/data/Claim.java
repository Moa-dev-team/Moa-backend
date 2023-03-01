package com.moa.sercurity.data;

import lombok.Getter;

@Getter
public enum Claim {
  AUTHORITIES_KEY("role"), EMAIL_KEY("email"), URL("localhost:8080"),
  ;

  private String key;

  Claim(String key) {
    this.key = key;
  }
}
