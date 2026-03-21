package com.company.cnpridepoint.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum ProgramType implements EnumClass<String> {

    ACADEMIC("Academic"),
    SPORTS("Sports"),
    CULTURAL("Cultural"),
    SOCIAL("Social"),
    CONFERENCE("Conference"),
    OTHER("Other");

    private final String id;

    ProgramType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static ProgramType fromId(String id) {
        for (ProgramType at : ProgramType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}