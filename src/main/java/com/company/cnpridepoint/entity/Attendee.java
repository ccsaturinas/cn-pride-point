package com.company.cnpridepoint.entity;

import io.jmix.core.DeletePolicy;
import io.jmix.core.FileRef;
import io.jmix.core.annotation.DeletedBy;
import io.jmix.core.annotation.DeletedDate;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.OnDelete;
import io.jmix.core.metamodel.annotation.DependsOnProperties;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.JmixProperty;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.Period;
import java.util.UUID;

@JmixEntity
@Table(name = "ATTENDEE", indexes = {
        @Index(name = "IDX_ATTENDEE_YEAR_LEVEL", columnList = "YEAR_LEVEL_ID"),
        @Index(name = "IDX_ATTENDEE_SECTION", columnList = "SECTION_ID")
})
@Entity
public class Attendee {
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

    @Column(name = "CODE")
    private String code;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @Column(name = "BIRTHDATE")
    private LocalDate birthdate;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "ATTENDEE_TYPE")
    private String attendeeType;

    @Column(name = "SHIRT_SIZE")
    private String shirtSize;

    @OnDelete(DeletePolicy.CASCADE)
    @JoinColumn(name = "YEAR_LEVEL_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private YearLevel yearLevel;

    @JoinColumn(name = "SECTION_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Section section;

    @Column(name = "PROFILE_PIC", length = 1024)
    private FileRef profilePic;

    @Column(name = "STATUS")
    private String status;

    public Status getStatus() {
        return status == null ? null : Status.fromId(status);
    }

    public void setStatus(Status status) {
        this.status = status == null ? null : status.getId();
    }

    public FileRef getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(FileRef profilePic) {
        this.profilePic = profilePic;
    }

    public Gender getGender() {
        return gender == null ? null : Gender.fromId(gender);
    }

    public void setGender(Gender gender) {
        this.gender = gender == null ? null : gender.getId();
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
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

    public ShirtSize getShirtSize() {
        return shirtSize == null ? null : ShirtSize.fromId(shirtSize);
    }

    public void setShirtSize(ShirtSize shirtSize) {
        this.shirtSize = shirtSize == null ? null : shirtSize.getId();
    }


    @InstanceName
    @JmixProperty
    @DependsOnProperties({"code", "lastName", "firstName", "middleName"})
    public String getDisplayName() {
        var ln = lastName == null ? "" : lastName.toUpperCase();
        var fn = firstName == null ? "" : firstName.toUpperCase();
        var mi = middleName == null || middleName.isEmpty() ? "" : middleName.substring(0, 1).toUpperCase() + ".";
        return String.format("%s - %s %s %s", (code != null ? code : ""), ln, fn, mi).trim();
    }

    @Transient
    @JmixProperty
    @DependsOnProperties("birthdate")
    public Integer getAge() {
        var dateNow = LocalDate.now();
        if (birthdate != null) {
            return Period.between(birthdate, dateNow).getYears();
        }
        return 0;
    }

    public AttendeeType getAttendeeType() {
        return attendeeType == null ? null : AttendeeType.fromId(attendeeType);
    }

    public void setAttendeeType(AttendeeType attendeeType) {
        this.attendeeType = attendeeType == null ? null : attendeeType.getId();
    }


    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

}