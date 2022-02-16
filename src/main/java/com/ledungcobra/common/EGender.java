package com.ledungcobra.common;

public enum EGender {
    None(0),
    Male(1),
    Female(2);

    private final int value;

    EGender(int val) {
        this.value = val;
    }

    public int getValue() {
        return value;
    }

    public static EGender from(int value){
        return switch (value) {
            case 0 -> EGender.None;
            case 1 -> EGender.Male;
            case 2 -> EGender.Female;
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }

}
