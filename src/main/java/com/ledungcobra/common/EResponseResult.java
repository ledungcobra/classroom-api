package com.ledungcobra.common;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EResponseResult {

    Error(0),
    Successful(1);

    private final int value;

    EResponseResult(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @JsonCreator
    public static EResponseResult fromInt(int val) {
        return switch (val) {
            case 0 -> Error;
            case 1 -> Successful;
            default -> throw new IllegalStateException("Unexpected value: " + val);
        };
    }
}
