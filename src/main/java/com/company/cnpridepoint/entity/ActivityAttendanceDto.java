package com.company.cnpridepoint.entity;

import io.jmix.core.MetadataTools;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.DependsOnProperties;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@JmixEntity
public class ActivityAttendanceDto {
    //    @JmixGeneratedValue
    @JmixId
    private UUID id;

    private ActivityScheduleDto activitySchedule;

    private LocalDateTime checkedInAt;

    private LocalDateTime checkedOutAt;

    private AttendeeDto attendee;

    private String attendeeType;

    private YearLevel yearLevel;

    private Section section;

    private String status;

    private String notes;

    private String mobileReference;


    public static ActivityAttendanceDto fromEntityActivityAttendance(ActivityAttendance entity) {
        if (entity == null) return null;
        ActivityAttendanceDto dto = new ActivityAttendanceDto();
        dto.setId(entity.getId());
        dto.setActivitySchedule(entity.getActivitySchedule());
        dto.setCheckedInAt(entity.getCheckedInAt());
        dto.setCheckedOutAt(entity.getCheckedOutAt());
        dto.setAttendee(entity.getAttendee());
        dto.setAttendeeType(entity.getAttendeeType());
        dto.setYearLevel(entity.getYearLevel());
        dto.setSection(entity.getSection());
        dto.setNotes(entity.getNotes());
        dto.setMobileReference(entity.getMobileReference());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public ActivityScheduleDto getActivitySchedule() {
        return activitySchedule;
    }

    public void setActivitySchedule(ActivitySchedule activitySchedule) {
        this.activitySchedule = ActivityScheduleDto.fromEntityActivitySchedule(activitySchedule);
    }

    public String getMobileReference() {
        return mobileReference;
    }

    public void setMobileReference(String mobileReference) {
        this.mobileReference = mobileReference;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public AttendeeType getAttendeeType() {
        return attendeeType == null ? null : AttendeeType.fromId(attendeeType);
    }

    public void setAttendeeType(AttendeeType attendeeType) {
        this.attendeeType = attendeeType == null ? null : attendeeType.getId();
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public YearLevel getYearLevel() {
        return yearLevel;
    }

    public void setYearLevel(YearLevel yearLevel) {
        this.yearLevel = yearLevel;
    }

    public AttendanceStatus getStatus() {
        return status == null ? null : AttendanceStatus.fromId(status);
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status == null ? null : status.getId();
    }

    public LocalDateTime getCheckedOutAt() {
        return checkedOutAt;
    }

    public void setCheckedOutAt(LocalDateTime checkedOutAt) {
        this.checkedOutAt = checkedOutAt;
    }

    public LocalDateTime getCheckedInAt() {
        return checkedInAt;
    }

    public void setCheckedInAt(LocalDateTime checkedInAt) {
        this.checkedInAt = checkedInAt;
    }

    public AttendeeDto getAttendee() {
        return attendee;
    }

    public void setAttendee(Attendee attendee) {
        this.attendee = attendee == null ? null : AttendeeDto.fromEntityAttendee(attendee);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @InstanceName
    @DependsOnProperties({"activitySchedule", "attendee"})
    public String getInstanceName(MetadataTools metadataTools) {
        return String.format("%s %s",
                metadataTools.format(activitySchedule),
                metadataTools.format(attendee));
    }


}