package com.cibersecurity.login.controller;

import com.cibersecurity.login.api.AuthApi;
import com.cibersecurity.login.dto.LoginDTO;
import com.cibersecurity.login.dto.TokenDTO;
import com.cibersecurity.login.dto.UserDTO;
import com.cibersecurity.login.error.exception.InputError;
import com.cibersecurity.login.error.exception.InputException;
import com.cibersecurity.login.mapper.UserMapper;
import com.cibersecurity.login.service.LoginService;
import com.cibersecurity.login.utils.HashService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
public class AuthController implements AuthApi {

    LoginService loginService;

    UserMapper userMapper;

    HashService hashService;

    @Override
    public TokenDTO login(LoginDTO loginDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            throwInputError(bindingResult);
        }

        loginDTO.setPassword(hashService.protectPassword(loginDTO.getPassword()));
        System.out.println(loginDTO);
        return loginService.login(loginDTO);
    }

    @Override
    public UserDTO createUser(UserDTO userDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            throwInputError(bindingResult);
        }
        userDTO.setRoleId(UUID.fromString("ea95d591-2590-4d83-8415-d492a0f681d4"));

        userDTO.setPassword(hashService.protectPassword(userDTO.getPassword()));
        return userMapper.fromUser(loginService.createUser(userMapper.fromDTO(userDTO), userDTO.getRoleId()));
    }

    private void throwInputError(BindingResult bindingResult){
        List<String> errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach(error -> {
            errors.add(error.getDefaultMessage());
        });
        throw new InputException(HttpStatus.BAD_REQUEST, new InputError(errors));
    }

}
