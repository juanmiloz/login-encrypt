package com.cibersecurity.login.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodesError {

    INVALID_USERNAME("CODE_01", "The username entered is not available"),
    USERNAME_NOT_FOUND("CODE_02", "The username you are looking for is not found, please verify the information you have entered"),
    SALT_NOT_FOUND("CODE_03", "The salt was not found in the database"),
    USER_NOT_FOUND("CODE_04", "The user was not found in the database"),
    INCORRECT_PASSWORD("CODE_05", "The password entered is incorrect, please verify the information you have entered"),
    ADMIN_NOT_DEFINED("CODE_06", "The admin user was not found in the database"),
    USER_NOT_ADMIN("CODE_07", "The user is not admin"),
    TOKEN_EXPIRED("CODE_08", "The token has expired, please log in again");

    private String code;
    private String message;
}
