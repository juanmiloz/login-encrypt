package com.cibersecurity.login.controller;

import com.cibersecurity.login.api.AuthApi;
import com.cibersecurity.login.dto.LoginDTO;
import com.cibersecurity.login.dto.TokenDTO;
import com.cibersecurity.login.dto.UserDTO;
import com.cibersecurity.login.error.exception.InputError;
import com.cibersecurity.login.error.exception.InputException;
import com.cibersecurity.login.error.exception.UserError;
import com.cibersecurity.login.error.exception.UserException;
import com.cibersecurity.login.mapper.UserMapper;
import com.cibersecurity.login.service.LoginService;
import com.cibersecurity.login.utils.HashService;
import com.cibersecurity.login.utils.JwtService;
import com.password4j.Hash;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
public class AuthController implements AuthApi {

    LoginService loginService;

    UserMapper userMapper;

    HashService hashService;

    JwtService jwtService;

    @Override
    public String login(LoginDTO loginDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            throwInputError(bindingResult);
        }

        loginDTO.setPassword(hashService.protectPassword(loginDTO.getPassword()));
        return jwtService.createJwt(loginService.login(loginDTO));
    }

    @Override
    public String createUser(UserDTO userDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            throwInputError(bindingResult);
        }

        userDTO.setPassword(hashService.protectPassword(userDTO.getPassword()));
        return jwtService.createJwt(loginService.createUser(userMapper.fromDTO(userDTO)));
    }

    private void throwInputError(BindingResult bindingResult){
        List<String> errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach(error -> {
            errors.add(error.getDefaultMessage());
        });
        throw new InputException(HttpStatus.BAD_REQUEST, new InputError(errors));
    }

}
