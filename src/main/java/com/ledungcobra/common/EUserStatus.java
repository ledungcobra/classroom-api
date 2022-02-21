package com.ledungcobra.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EUserStatus {
    NotSpecified(0),
    Active(1),
    Locked(50),
    Inactive(99),
    ;

    private final int value;

    EUserStatus(int val) {
        this.value = val;
    }

    @JsonProperty
    public int getValue() {
        return value;
    }

    public static EUserStatus from(int value) {
        return switch (value) {
            case 0 -> NotSpecified;
            case 1 -> Active;
            case 50 -> Locked;
            case 99 -> Inactive;
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }

}