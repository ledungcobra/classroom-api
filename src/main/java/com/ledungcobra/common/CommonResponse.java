package com.ledungcobra.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.io.Serial;
import java.io.Serializable;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.badRequest;

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

    public static ResponseEntity<CommonResponse<?>> buildError(BindingResult result) {
        return badRequest().body(
                CommonResponse.builder()
                        .status(EStatus.Error)
                        .result(EResult.Error)
                        .content(String.join(",",
                                result.getAllErrors().stream().map(ObjectError::toString)
                                        .collect(Collectors.toSet())))
                        .build()
        );
    }

}
