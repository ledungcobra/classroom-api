package com.ledungcobra.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum ERole {
    None(0),
    Teacher(1),
    Student(2);

    private final int role;

    ERole(int role) {
        this.role = role;
    }

    @JsonProperty
    public int getValue() {
        return role;
    }

    @JsonCreator
    public static ERole fromInt(Integer value) {
        return switch (value) {
            case 0 -> ERole.None;
            case 1 -> ERole.Teacher;
            case 2 -> ERole.Student;
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }
}
