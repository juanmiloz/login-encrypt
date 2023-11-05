package com.cibersecurity.login.error.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class InputError {
    List<String> inputErrors;
}
