package com.moa.moa3.entity.member.profile;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum Category {
    PYTHON("Python"), JAVA("Java"), REACT("React"), JAVASCRIPT("JavaScript"), SPRING("Spring"),
    FRONTEND("Frontend"), BACKEND("Backend"), STUDENT("Student");

    private final String name;
    Category(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    static public Category of(String name) {
        for(Category category : Category.values()) {
            if(category.getName().equals(name)) {
                return category;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 카테고리입니다. : " + name);
    }
}
