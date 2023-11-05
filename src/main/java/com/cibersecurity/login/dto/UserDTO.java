package com.cibersecurity.login.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @NotBlank(message = "The username cannot be empty, please enter a valid username.")
    private String username;

    @NotBlank(message = "The password cannot be empty, please enter a valid password.")
    private String password;

    private String lastDate;

}
