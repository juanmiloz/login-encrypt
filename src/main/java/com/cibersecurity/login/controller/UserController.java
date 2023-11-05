package com.cibersecurity.login.controller;

import com.cibersecurity.login.api.UserApi;
import com.cibersecurity.login.dto.UpdateUserDTO;
import com.cibersecurity.login.dto.UserDTO;
import com.cibersecurity.login.mapper.UserMapper;
import com.cibersecurity.login.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
public class UserController implements UserApi {

    UserService userService;

    UserMapper userMapper;

    @Override
    public List<UserDTO> getUsers() {
        return userService.getUsers().stream().map(userMapper::fromUser).collect(Collectors.toList());
    }

    @Override
    public UserDTO deleteUser(String username) {
        return userMapper.fromUser(userService.deleteUser(username));
    }

    @Override
    public UserDTO updateUser(UpdateUserDTO updateUserDTO) {
        return userMapper.fromUser(userService.updateUser(userMapper.fromUpdateUser(updateUserDTO)));
    }

}
