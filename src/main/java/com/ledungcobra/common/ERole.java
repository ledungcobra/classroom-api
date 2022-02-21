package com.ledungcobra.common;

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
    public int getRole() {
        return role;
    }
}
