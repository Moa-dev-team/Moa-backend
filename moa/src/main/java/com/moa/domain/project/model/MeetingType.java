package com.moa.domain.project.model;

public enum MeetingType {
    FACE_TO_FACE("오프라인 대면"), ONLINE("온라인 비대면"), FREE("상관없음"),
    ;
    private String key;

    MeetingType(String key) {
        this.key = key;
    }
}
