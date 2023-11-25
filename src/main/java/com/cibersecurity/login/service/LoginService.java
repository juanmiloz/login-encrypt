package com.cibersecurity.login.service;

import com.cibersecurity.login.dto.LoginDTO;
import com.cibersecurity.login.dto.TokenDTO;
import com.cibersecurity.login.dto.UserDTO;
import com.cibersecurity.login.model.User;

import java.util.UUID;

public interface LoginService {
    TokenDTO login(LoginDTO loginDTO);

    User createUser(User user, UUID roleId);
}
