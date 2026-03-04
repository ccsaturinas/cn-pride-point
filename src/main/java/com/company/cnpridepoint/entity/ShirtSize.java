package com.company.cnpridepoint.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;


public enum ShirtSize implements EnumClass<String> {

    EXTRA_SMALL("XS"),
    SMALL("S"),
    MEDIUM("M"),
    LARGE("L"),
    EXTRA_LARGE("XL"),
    DOUBLE_XL("XXL"),
    TRIBLE_XL("XXXL");

    private final String id;

    ShirtSize(String id) {
        this.id = id;
    }

    public  @NonNull String getId() {
        return id;
    }

    @Nullable
    public static ShirtSize fromId(String id) {
        for (ShirtSize at : ShirtSize.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}