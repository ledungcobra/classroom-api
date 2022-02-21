package com.ledungcobra.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum EApiStatus {
    Success(200),
    Error(400);

    private final int value;

    EApiStatus(int value) {
        this.value = value;
    }

    @JsonProperty
    public int getValue() {
        return value;
    }

    @JsonCreator
    public static EApiStatus fromInt(int val) {
        return switch (val) {
            case 200 -> Success;
            case 400 -> Error;
            default -> throw new IllegalStateException("Unexpected value: " + val);
        };
    }
}
