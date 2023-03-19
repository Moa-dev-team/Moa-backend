package com.moa.domain.project.value;

public enum ProjectStatus {
    OPENING("모집중"), CLOSED("마감"),
    ;
    private String key;

    ProjectStatus(String key) {
        this.key = key;
    }
}
