package com.company.cnpridepoint.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum ActivityType implements EnumClass<String> {

    EVENT("Event"),
    PROGRAM("Program"),
    SEMINAR("Seminar"),
    OTHERS("Others");

    private final String id;

    ActivityType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static ActivityType fromId(String id) {
        for (ActivityType at : ActivityType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}