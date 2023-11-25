package com.cibersecurity.login.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodesError {

    INVALID_USERNAME("CODE_01", "The username entered is not available."),
    USERNAME_NOT_FOUND("CODE_02", "The username you are looking for is not found, please verify the information you have entered."),
    SALT_NOT_FOUND("CODE_03", "The salt was not found in the database."),
    VERIFY_INFO("CODE_04","The user or password does not match, please verify the data entered."),
    UNAUTHORIZED("401","unauthorized: error must be authenticated in order to make the request."),
    NON_EXISTENT_ROLE("CODE_05", "Role not found, please contact an administrator."),
    UNAUTHORIZED_REQUEST("CODE_06","You are not authorized to make this request"),
    ROLE_NOT_FOUND("CODE_07", "The role you were looking for was not found, please contact customer service."),
    INVALID_CREDENTIALS("CODE_08", "Invalid login credentials, please verify the data entered.");

    private String code;
    private String message;
}
