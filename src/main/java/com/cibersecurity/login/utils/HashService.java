package com.cibersecurity.login.utils;

import com.cibersecurity.login.constants.CodesError;
import com.cibersecurity.login.error.exception.UserError;
import com.cibersecurity.login.error.exception.UserException;
import com.cibersecurity.login.model.Configuration;
import com.cibersecurity.login.repository.ConfigurationRepository;
import com.password4j.Hash;
import com.password4j.Password;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class HashService {

    ConfigurationRepository configurationRepository;

    public String protectPassword(String userPassword) {
        Configuration configuration = configurationRepository.findById("salt").orElseThrow(() -> {
            throw new UserException(HttpStatus.BAD_REQUEST, new UserError(CodesError.SALT_NOT_FOUND.getCode(), CodesError.SALT_NOT_FOUND.getMessage()));
        });

        Hash hash = Password.hash(userPassword)
                .addSalt(configuration.getValue())
                .withPBKDF2();

        return hash.getResult();
    }

    public boolean checkPassword(String password, String hash){
        Configuration configuration = configurationRepository.findById("salt").orElseThrow(() -> {
            throw new UserException(HttpStatus.BAD_REQUEST, new UserError(CodesError.SALT_NOT_FOUND.getCode(), CodesError.SALT_NOT_FOUND.getMessage()));
        });

        return Password.check(password, hash).addSalt(configuration.getValue()).withPBKDF2();
    }
}
