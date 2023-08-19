package com.moa.moa3.entity.member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Profile {
    @Id @GeneratedValue
    private Long id;
}
