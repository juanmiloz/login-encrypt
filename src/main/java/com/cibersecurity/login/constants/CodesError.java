package com.cibersecurity.login.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodesError {

    INVALID_USERNAME("CODE_01", "The username entered is not available"),
    USERNAME_NOT_FOUND("CODE_02", "The username you are looking for is not found, please verify the information you have entered"),
    SALT_NOT_FOUND("CODE_03", "The salt was not found in the database");

    private String code;
    private String message;
}
