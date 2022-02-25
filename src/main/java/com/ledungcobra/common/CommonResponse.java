package com.ledungcobra.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CommonResponse<T extends Serializable> implements Serializable {

    @Serial
    public static final long serializableVersionUID = 1L;

    private EStatus status;
    private EResult result;
    private String message;
    private T content;

    public int getStatus() {
        return status.getValue();
    }

    public int getResult() {
        return result.getValue();
    }

}
