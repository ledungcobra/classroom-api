package com.ledungcobra.common;

public enum EResponseResult {
    Error(0),
    Successful(1);

    private final int result;

    EResponseResult(int value) {
        this.result = value;
    }

    public int getResult() {
        return result;
    }
}
