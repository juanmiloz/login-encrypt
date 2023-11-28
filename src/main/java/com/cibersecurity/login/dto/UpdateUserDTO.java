package com.cibersecurity.login.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDTO {

    @NotBlank(message = "The username cannot be empty, please enter a valid username.")
    private String username;

    @NotNull(message = "The password cannot be null, please enter a valid password")
    private String password;

    private String lastDate;

    private UUID roleId;
}
