package com.cibersecurity.login.controller;

import com.cibersecurity.login.api.UserApi;
import com.cibersecurity.login.constants.CodesError;
import com.cibersecurity.login.dto.UpdateUserDTO;
import com.cibersecurity.login.dto.UserDTO;
import com.cibersecurity.login.error.exception.UserError;
import com.cibersecurity.login.error.exception.UserException;
import com.cibersecurity.login.mapper.UserMapper;
import com.cibersecurity.login.security.SecurityContext;
import com.cibersecurity.login.security.SecurityContextHolder;
import com.cibersecurity.login.service.UserService;
import com.cibersecurity.login.utils.HashService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
public class UserController implements UserApi {

    UserService userService;

    UserMapper userMapper;

    HashService hashService;

    @Override
    public List<UserDTO> getUsers() {
        return userService.getUsers().stream().map(userMapper::fromUser).collect(Collectors.toList());
    }

    @Override
    public UserDTO getOwnInfo() {
        String username = SecurityContextHolder.getContext().getUserId();
        return userMapper.fromUser(userService.getUser(username));
    }

    @Override
    public UserDTO updateOwnInfo(UpdateUserDTO updateUserDTO) {
        String username = SecurityContextHolder.getContext().getUserId();
        if(username.equals(updateUserDTO.getUsername())){
            updateUserDTO.setPassword(hashService.protectPassword(updateUserDTO.getPassword()));
            return userMapper.fromUser(userService.updateUser(userMapper.fromUpdateUserDTO(updateUserDTO),updateUserDTO.getRoleId()));
        }
        throw new UserException(HttpStatus.BAD_REQUEST, new UserError(CodesError.UNAUTHORIZED_REQUEST.getCode(), CodesError.UNAUTHORIZED_REQUEST.getMessage()));
    }

    @Override
    public UserDTO deleteUser(String username) {
        return userMapper.fromUser(userService.deleteUser(username));
    }

    @Override
    public UserDTO updateUser(UpdateUserDTO updateUserDTO) {
        updateUserDTO.setPassword(hashService.protectPassword(updateUserDTO.getPassword()));
        return userMapper.fromUser(userService.updateUser(userMapper.fromUpdateUserDTO(updateUserDTO),updateUserDTO.getRoleId()));
    }

}
