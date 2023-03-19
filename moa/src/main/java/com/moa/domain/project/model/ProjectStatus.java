package com.moa.domain.project.model;

public enum ProjectStatus {
    OPENING("모집중"), CLOSED("마감"),
    ;
    private String key;

    ProjectStatus(String key) {
        this.key = key;
    }
}
