package com.cibersecurity.login.service;

import com.cibersecurity.login.dto.TokenDTO;
import com.cibersecurity.login.model.User;

import java.util.List;

public interface UserService {

    List<User> getUsers(TokenDTO token);

    User deleteUser(TokenDTO token, String username);

    User updateUser(TokenDTO token, User user);
}
