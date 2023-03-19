package com.moa.domain.project.entity;

import com.moa.domain.project.value.MeetingType;
import com.moa.domain.project.value.ProjectStatus;
import com.moa.domain.utility.BaseEntity;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "introduce_content")
    private String introduceContent;

    @Column(name = "requirement_content")
    private String requirementContent;

    @Column(name = "recruit_number")
    private Integer recruitNumber;

    @Column(name = "current_number")
    private Integer currentNumber;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "visit_count")
    private Integer visitCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_status")
    private ProjectStatus projectStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "meeting_type")
    private MeetingType meetingType;
}
