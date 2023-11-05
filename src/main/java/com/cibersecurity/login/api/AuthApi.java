package com.cibersecurity.login.api;

import com.cibersecurity.login.dto.LoginDTO;
import com.cibersecurity.login.dto.TokenDTO;
import com.cibersecurity.login.dto.UserDTO;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public interface AuthApi {

    @PostMapping()
    TokenDTO login(@RequestBody @Valid LoginDTO loginDTO, BindingResult bindingResult);

    @PostMapping("/create")
    UserDTO createUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult);
}
