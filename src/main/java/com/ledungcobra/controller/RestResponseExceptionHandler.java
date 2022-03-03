package com.ledungcobra.controller;

import com.ledungcobra.common.CommonResponse;
import com.ledungcobra.common.EResult;
import com.ledungcobra.common.EStatus;
import com.ledungcobra.exception.MyAuthenticationException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class RestResponseExceptionHandler {

    @ExceptionHandler(value = {MyAuthenticationException.class, ExpiredJwtException.class})
    public ResponseEntity<?> handleException(MyAuthenticationException exception, WebRequest request) {
        log.info("Exception {}", exception.getMessage());
        var headerName = request.getHeaderNames();

        while (headerName.hasNext()) {
            var header = headerName.next();
            log.info("Header name {}", header);
            var value = request.getHeader(header);
            log.info("Value {}", value);
        }
        return new ResponseEntity<>(CommonResponse.builder()
                .status(EStatus.AuthenticationFail)
                .result(EResult.Error)
                .message("Authentication failed")
                .build(), HttpStatus.UNAUTHORIZED);
    }

}
