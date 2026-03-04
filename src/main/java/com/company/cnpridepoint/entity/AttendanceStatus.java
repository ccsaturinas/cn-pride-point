package com.company.cnpridepoint.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum AttendanceStatus implements EnumClass<String> {

    PRESENT("Present"),
    LATE("Late"),
    ABSENT("Absent");

    private final String id;

    AttendanceStatus(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static AttendanceStatus fromId(String id) {
        for (AttendanceStatus at : AttendanceStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}