package com.ledungcobra.common;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SingleResponse<T> implements Serializable {
    private T result;
}
