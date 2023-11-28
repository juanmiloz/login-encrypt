package com.cibersecurity.login.service.impl;

import com.cibersecurity.login.constants.CodesError;
import com.cibersecurity.login.error.exception.UserError;
import com.cibersecurity.login.error.exception.UserException;
import com.cibersecurity.login.model.Role;
import com.cibersecurity.login.model.User;
import com.cibersecurity.login.repository.RoleRepository;
import com.cibersecurity.login.repository.UserRepository;
import com.cibersecurity.login.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    RoleRepository roleRepository;

    @Override
    public List<User> getUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public User getUser(String username) {
        return userRepository.findById(username).orElseThrow(() -> {
            throw new UserException(HttpStatus.BAD_REQUEST, new UserError(CodesError.USERNAME_NOT_FOUND.getCode(), CodesError.USERNAME_NOT_FOUND.getMessage()));
        });
    }

    @Override
    public User deleteUser(String username) {
        User userFound = userRepository.findById(username).orElseThrow(() -> {
            throw new UserException(HttpStatus.BAD_REQUEST, new UserError(CodesError.USERNAME_NOT_FOUND.getCode(), CodesError.USERNAME_NOT_FOUND.getMessage()));
        });
        userRepository.deleteById(username);
        return userFound;
    }

    @Override
    public User updateUser(User user, UUID roleId) {
        userRepository.findById(user.getUsername()).orElseThrow(() -> {
            throw new UserException(HttpStatus.BAD_REQUEST, new UserError(CodesError.USERNAME_NOT_FOUND.getCode(), CodesError.USERNAME_NOT_FOUND.getMessage()));
        });

        Role roleFound = roleRepository.findById(roleId).orElseThrow(() -> {
            throw new UserException(HttpStatus.BAD_REQUEST, new UserError(CodesError.ROLE_NOT_FOUND.getCode(), CodesError.ROLE_NOT_FOUND.getMessage()));
        });

        user.setRole(roleFound);

        return userRepository.save(user);
    }
}
