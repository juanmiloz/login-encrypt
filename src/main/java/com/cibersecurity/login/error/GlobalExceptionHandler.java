package com.cibersecurity.login.error;

import com.cibersecurity.login.error.exception.InputError;
import com.cibersecurity.login.error.exception.InputException;
import com.cibersecurity.login.error.exception.UserError;
import com.cibersecurity.login.error.exception.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<UserError>handlerException(UserException userException){
        return new ResponseEntity<>(userException.getError(), userException.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<InputError>handlerException(InputException inputException){
        return new ResponseEntity<>(inputException.getError(), inputException.getHttpStatus());
    }
}
