package com.ledungcobra.common;

public enum EApiStatus {
    Success(200),
    Error(400);

    private final int status;

    EApiStatus(int value) {
        this.status = value;
    }

    public int getStatus() {
        return status;
    }
}
