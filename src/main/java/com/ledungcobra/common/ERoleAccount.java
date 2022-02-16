package com.ledungcobra.common;

public enum ERoleAccount {

    Admin(1), User(0);

    private final int value;
    ERoleAccount(int val) {
        this.value = val;
    }

    public int getValue() {
        return value;
    }
    public static ERoleAccount from(int value){
        return switch (value){
            case 0 -> ERoleAccount.User;
            case 1 -> ERoleAccount.Admin;
            default -> throw new IllegalStateException("Unexpected value: " +value);
        };
    }
}
