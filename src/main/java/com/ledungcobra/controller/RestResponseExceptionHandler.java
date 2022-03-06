package com.ledungcobra.controller;

import com.ledungcobra.common.CommonResponse;
import com.ledungcobra.common.EResult;
import com.ledungcobra.common.EStatus;
import com.ledungcobra.exception.MyAuthenticationException;
import com.ledungcobra.exception.StudentIdAlreadyExistException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class RestResponseExceptionHandler {

    @ExceptionHandler(value = {MyAuthenticationException.class, ExpiredJwtException.class})
    public ResponseEntity<?> handleException(Exception exception) {
        log.info("Exception {}", exception.getMessage());
        return new ResponseEntity<>(CommonResponse.builder()
                .status(EStatus.AuthenticationFail)
                .result(EResult.Error)
                .message("Authentication failed")
                .build(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public ResponseEntity<?> handleUserNotFoundException(UsernameNotFoundException exception) {
        log.info("Exception {}", exception.getMessage());
        return new ResponseEntity<>(CommonResponse.builder()
                .status(EStatus.AuthenticationFail)
                .result(EResult.Error)
                .message(exception.getMessage())
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {StudentIdAlreadyExistException.class})
    public ResponseEntity<?> userIdAlreadyExists(StudentIdAlreadyExistException exception) {
        log.info("Exception {}", exception.getMessage());
        return new ResponseEntity<>(CommonResponse.builder()
                .status(EStatus.AuthenticationFail)
                .result(EResult.Error)
                .message(exception.getMessage())
                .build(), HttpStatus.NOT_FOUND);
    }


}
