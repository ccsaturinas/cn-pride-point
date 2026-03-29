package com.company.cnpridepoint.entity;

import io.jmix.core.FileRef;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.DependsOnProperties;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.JmixProperty;
import jakarta.persistence.Transient;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@JmixEntity
public class AttendeeDto {
    @JmixId
    private UUID id;

    private String code;

    private String lastName;

    private String firstName;

    private String middleName;

    private LocalDate birthdate;

    private String gender;

    private String attendeeType;

    private String shirtSize;


    private YearLevel yearLevel;

    private Section section;

    private FileRef profilePic;

    private String status;


    public static AttendeeDto fromEntityAttendee(Attendee entity) {
        if (entity == null) return null;
        AttendeeDto dto = new AttendeeDto();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setLastName(entity.getLastName());
        dto.setFirstName(entity.getFirstName());
        dto.setMiddleName(entity.getMiddleName());
        dto.setBirthdate(entity.getBirthdate());
        dto.setGender(entity.getGender());
        dto.setAttendeeType(entity.getAttendeeType());
        dto.setShirtSize(entity.getShirtSize());
//        dto.setYearLevel(entity.getYearLevel());
//        dto.setSection(entity.getSection());
        dto.setProfilePic(entity.getProfilePic());
        dto.setStatus(entity.getStatus());
        return dto;
    }

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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}