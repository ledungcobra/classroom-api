package com.ledungcobra.common;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ETypeNotification {

    ForStudent(1),
    ForTeacher(2);

    private final byte value;

    ETypeNotification(int value) {
        this.value = (byte) value;
    }

    @JsonValue
    public byte getValue() {
        return value;
    }

    public static ETypeNotification from(int value){
        return switch ((byte)value){
            case (byte)1 -> ETypeNotification.ForStudent;
            case (byte) 2-> ETypeNotification.ForTeacher;
            default -> throw new IllegalStateException("Unexpected value: " + (byte) value);
        };
    }
}
