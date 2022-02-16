package com.ledungcobra.common;

public enum ERole {
    None(0),
    Teacher(1),
    Student(2);

    private final int role;

    ERole(int role) {
        this.role = role;
    }

    public int getRole() {
        return role;
    }
}
