package com.moa.moa3.entity.member.profile;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Category {
    @Id @GeneratedValue
    private Long id;
    private String name;

    public Category(String name) {
        this.name = name;
    }
}
