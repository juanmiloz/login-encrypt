package com.cibersecurity.login.error.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class UserException extends RuntimeException{
    HttpStatus httpStatus;
    UserError error;
}
