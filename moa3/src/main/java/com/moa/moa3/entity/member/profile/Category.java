package com.moa.moa3.entity.member.profile;

import lombok.Getter;

@Getter
public enum Category {
    PYTHON("Python"), JAVA("Java"), REACT("React"), JAVASCRIPT("JavaScript"), SPRING("Spring");

    private final String name;
    Category(String name) {
        this.name = name;
    }

    static public Category of(String name) {
        for(Category category : Category.values()) {
            if(category.getName().equals(name)) {
                return category;
            }
        }
        throw new IllegalArgumentException("No such category exists");
    }
}
