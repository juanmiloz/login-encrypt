package com.cibersecurity.login.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @NotBlank(message = "The username cannot be empty, please enter a valid username.")
    private String username;

    @NotBlank(message = "The password cannot be empty, please enter a valid password.")
    @Pattern(regexp = ".*[A-Z].*", message = "La contraseña debe contener al menos una letra mayúscula.")
    @Pattern(regexp = ".*[0-9].*", message = "La contraseña debe contener al menos un dígito.")
    @Pattern(regexp = ".*[^a-zA-Z0-9].*", message = "La contraseña debe contener al menos un carácter especial.")
    @Size(min = 8, message = "La longitud mínima de la contraseña debe ser 8 caracteres.")
    private String password;

    private String lastDate;

    private UUID roleId;

}
