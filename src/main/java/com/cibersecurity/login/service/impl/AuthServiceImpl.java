package com.cibersecurity.login.service.impl;

import com.cibersecurity.login.constants.CodesError;
import com.cibersecurity.login.dto.LoginDTO;
import com.cibersecurity.login.dto.TokenDTO;
import com.cibersecurity.login.error.exception.UserError;
import com.cibersecurity.login.error.exception.UserException;
import com.cibersecurity.login.model.User;
import com.cibersecurity.login.repository.UserRepository;
import com.cibersecurity.login.service.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements LoginService {

    UserRepository userRepository;

    @Override
    public TokenDTO login(LoginDTO loginDTO) {
        return null;
    }

    @Override
    public User createUser(User user) {
        userRepository.findById(user.getUsername()).ifPresent(userFound -> {
            throw new UserException(HttpStatus.BAD_REQUEST, new UserError(CodesError.INVALID_USERNAME.getCode(), CodesError.INVALID_USERNAME.getMessage()));
        });
        return userRepository.save(user);
    }
}
