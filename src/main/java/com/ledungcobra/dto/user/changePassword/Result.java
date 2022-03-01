package com.ledungcobra.dto.user.changePassword;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {
    private boolean succeeded;

    public Result(boolean succeeded) {
        this.succeeded = succeeded;
    }
}
