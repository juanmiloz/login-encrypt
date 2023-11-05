package com.cibersecurity.login.error.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserError {
    String code;
    String message;
}
