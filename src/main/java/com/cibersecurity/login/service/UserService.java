package com.cibersecurity.login.service;

import com.cibersecurity.login.model.User;

import java.util.List;

public interface UserService {

    List<User> getUsers();

    User deleteUser(String username);

    User updateUser(User user);
}
