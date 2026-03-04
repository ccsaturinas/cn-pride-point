package com.company.cnpridepoint.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;


public enum AttendeeType implements EnumClass<String> {

    STUDENT("Student"),
    FACULTY("Faculty"),
    STAFF("Staff"),
    GUEST("Guest"),
    SPONSOR("Sponsor"),
    VISITOR("Visitor");

    private final String id;

    AttendeeType(String id) {
        this.id = id;
    }

    public @NonNull String  getId() {
        return id;
    }

    @Nullable
    public static AttendeeType fromId(String id) {
        for (AttendeeType at : AttendeeType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}