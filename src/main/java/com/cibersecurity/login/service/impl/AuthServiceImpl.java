package com.cibersecurity.login.service.impl;

import antlr.Token;
import com.cibersecurity.login.constants.CodesError;
import com.cibersecurity.login.dto.LoginDTO;
import com.cibersecurity.login.dto.TokenDTO;
import com.cibersecurity.login.error.exception.UserError;
import com.cibersecurity.login.error.exception.UserException;
import com.cibersecurity.login.model.User;
import com.cibersecurity.login.repository.ConfigurationRepository;
import com.cibersecurity.login.repository.UserRepository;
import com.cibersecurity.login.service.LoginService;
import com.cibersecurity.login.utils.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements LoginService {

    private UserRepository userRepository;
    private ConfigurationRepository configurationRepository;

    @Override
    public TokenDTO login(LoginDTO loginDTO) {
        User user = userRepository.findById(loginDTO.getUsername()).orElseThrow(() -> {
            throw new UserException(HttpStatus.BAD_REQUEST, new UserError(CodesError.USER_NOT_FOUND.getCode(), CodesError.USER_NOT_FOUND.getMessage()));
        });
        String admin = configurationRepository.findById("admin").orElseThrow(() -> {
            throw new UserException(HttpStatus.BAD_REQUEST, new UserError(CodesError.ADMIN_NOT_DEFINED.getCode(), CodesError.ADMIN_NOT_DEFINED.getMessage()));
        }).getValue();
        TokenDTO tokenDTO = new TokenDTO();
        if(user.getPassword().equals(loginDTO.getPassword())){
            tokenDTO = new TokenDTO();
            tokenDTO.setUserName(user.getUsername());
            tokenDTO.setAdmin(user.getUsername().equals(admin));
        } else {
            throw new UserException(HttpStatus.BAD_REQUEST, new UserError(CodesError.INCORRECT_PASSWORD.getCode(), CodesError.INCORRECT_PASSWORD.getMessage()));
        }
        user.setLastDate(new Date(System.currentTimeMillis()).toString());
        userRepository.save(user);
        return tokenDTO;
    }

    @Override
    public TokenDTO createUser(User user) {
        userRepository.findById(user.getUsername()).ifPresent(userFound -> {
            throw new UserException(HttpStatus.BAD_REQUEST, new UserError(CodesError.INVALID_USERNAME.getCode(), CodesError.INVALID_USERNAME.getMessage()));
        });
        user.setLastDate(new Date(System.currentTimeMillis()).toString());
        userRepository.save(user);
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setUserName(user.getUsername());
        tokenDTO.setAdmin(false);
        return tokenDTO;
    }
}
