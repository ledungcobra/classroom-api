package com.ledungcobra.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EGradeReviewStatus {
    None(0),
    Pending(1),
    Approve(2),
    Reject(3);

    private final int value;

    EGradeReviewStatus(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    @JsonCreator
    public static EGradeReviewStatus from(int value) {
        return switch (value) {
            case 0 -> EGradeReviewStatus.None;
            case 1 -> EGradeReviewStatus.Pending;
            case 2 -> EGradeReviewStatus.Approve;
            case 3 -> EGradeReviewStatus.Reject;
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }
}
