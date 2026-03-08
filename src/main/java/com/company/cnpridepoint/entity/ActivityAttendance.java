package com.company.cnpridepoint.entity;

import io.jmix.core.DeletePolicy;
import io.jmix.core.MetadataTools;
import io.jmix.core.annotation.DeletedBy;
import io.jmix.core.annotation.DeletedDate;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.OnDelete;
import io.jmix.core.entity.annotation.OnDeleteInverse;
import io.jmix.core.metamodel.annotation.DependsOnProperties;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@JmixEntity
@Table(name = "ACTIVITY_ATTENDANCE", indexes = {
        @Index(name = "IDX_ACTIVITY_ATTENDANCE_ACTIVITY", columnList = "ACTIVITY_ID"),
        @Index(name = "IDX_ACTIVITY_ATTENDANCE_ATTENDEE", columnList = "ATTENDEE_ID"),
        @Index(name = "IDX_ACTIVITY_ATTENDANCE_YEAR_LEVEL", columnList = "YEAR_LEVEL_ID"),
        @Index(name = "IDX_ACTIVITY_ATTENDANCE_SECTION", columnList = "SECTION_ID"),
        @Index(name = "IDX_ACTIVITY_ATTENDANCE_PROGRAM", columnList = "PROGRAM_ID")
})
@Entity
public class ActivityAttendance {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "VERSION", nullable = false)
    @Version
    private Integer version;

    @CreatedBy
    @Column(name = "CREATED_BY")
    private String createdBy;

    @CreatedDate
    @Column(name = "CREATED_DATE")
    private OffsetDateTime createdDate;

    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY")
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE")
    private OffsetDateTime lastModifiedDate;

    @DeletedBy
    @Column(name = "DELETED_BY")
    private String deletedBy;

    @DeletedDate
    @Column(name = "DELETED_DATE")
    private OffsetDateTime deletedDate;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @JoinColumn(name = "PROGRAM_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Program program;

    @OnDelete(DeletePolicy.UNLINK)
    @JoinColumn(name = "ACTIVITY_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Activity activity;

    @Column(name = "CHECKED_IN_AT")
    private LocalDateTime checkedInAt;

    @Column(name = "CHECKED_OUT_AT")
    private LocalDateTime checkedOutAt;

    @OnDelete(DeletePolicy.UNLINK)
    @JoinColumn(name = "ATTENDEE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Attendee attendee;

    @Column(name = "ATTENDEE_TYPE")
    private String attendeeType;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @JoinColumn(name = "YEAR_LEVEL_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private YearLevel yearLevel;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @JoinColumn(name = "SECTION_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Section section;

    @Column(name = "STATUS")
    private String status;

    public AttendeeType getAttendeeType() {
        return attendeeType == null ? null : AttendeeType.fromId(attendeeType);
    }

    public void setAttendeeType(AttendeeType attendeeType) {
        this.attendeeType = attendeeType == null ? null : attendeeType.getId();
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
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

    public Attendee getAttendee() {
        return attendee;
    }

    public void setAttendee(Attendee attendee) {
        this.attendee = attendee;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public OffsetDateTime getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(OffsetDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public OffsetDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(OffsetDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(OffsetDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @InstanceName
    @DependsOnProperties({"program", "activity"})
    public String getInstanceName(MetadataTools metadataTools) {
        return String.format("%s %s",
                metadataTools.format(program),
                metadataTools.format(activity));
    }
}