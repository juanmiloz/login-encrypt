package com.cibersecurity.login.controller;

import com.cibersecurity.login.api.UserApi;
import com.cibersecurity.login.dto.TokenDTO;
import com.cibersecurity.login.dto.UpdateUserDTO;
import com.cibersecurity.login.dto.UserDTO;
import com.cibersecurity.login.mapper.UserMapper;
import com.cibersecurity.login.service.UserService;
import com.cibersecurity.login.utils.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
public class UserController implements UserApi {

    UserService userService;

    UserMapper userMapper;

    JwtService jwtService;

    @Override
    public List<UserDTO> getUsers(String token) {
        TokenDTO tokenDTO = jwtService.getValue(token);
        return userService.getUsers(tokenDTO).stream().map(userMapper::fromUser).collect(Collectors.toList());
    }

    @Override
    public UserDTO deleteUser(String token, String username) {
        TokenDTO tokenDTO = jwtService.getValue(token);
        return userMapper.fromUser(userService.deleteUser(tokenDTO,username));
    }

    @Override
    public UserDTO updateUser(String token, UpdateUserDTO updateUserDTO) {
        TokenDTO tokenDTO = jwtService.getValue(token);
        return userMapper.fromUser(userService.updateUser(tokenDTO,userMapper.fromUpdateUser(updateUserDTO)));
    }

}
