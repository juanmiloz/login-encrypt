package com.cibersecurity.login.service;

import com.cibersecurity.login.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<User> getUsers();

    User getUser(String username);

    User deleteUser(String username);

    User updateUser(User user, UUID roleId);
}
