package com.company.cnpridepoint.entity;

import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@JmixEntity
public class ActivityScheduleDto {

    @JmixId
    private UUID id;

    @InstanceName
    private Program program;

    private Activity activity;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String status;

    private String notes;

    public static ActivityScheduleDto fromEntityActivitySchedule(ActivitySchedule entity) {
        if (entity == null) return null;
        ActivityScheduleDto dto = new ActivityScheduleDto();
        dto.setId(entity.getId());
        dto.setProgram(entity.getProgram());
        dto.setActivity(entity.getActivity());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setStatus(entity.getStatus());
        dto.setNotes(entity.getNotes());
        return dto;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status == null ? null : status.getId();
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}