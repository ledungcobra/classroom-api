package com.ledungcobra.common;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ETypeNotification {

    ForStudent(1),
    ForTeacher(2);

    private final int value;

    ETypeNotification(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    public static ETypeNotification from(int value){
        return switch (value){
            case 1 -> ETypeNotification.ForStudent;
            case  2-> ETypeNotification.ForTeacher;
            default -> throw new IllegalStateException("Unexpected value: " + (byte) value);
        };
    }
}
