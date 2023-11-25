package com.cibersecurity.login.service.impl;

import com.cibersecurity.login.constants.CodesError;
import com.cibersecurity.login.constants.TokenTime;
import com.cibersecurity.login.dto.LoginDTO;
import com.cibersecurity.login.dto.TokenDTO;
import com.cibersecurity.login.error.exception.UserError;
import com.cibersecurity.login.error.exception.UserException;
import com.cibersecurity.login.model.Role;
import com.cibersecurity.login.model.User;
import com.cibersecurity.login.repository.RoleRepository;
import com.cibersecurity.login.repository.UserRepository;
import com.cibersecurity.login.service.LoginService;
import com.cibersecurity.login.utils.JWTParser;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.BoundedReferenceType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements LoginService {

    UserRepository userRepository;

    RoleRepository roleRepository;

    @Override
    public TokenDTO login(LoginDTO loginDTO) {
        User user = userRepository.findById(loginDTO.getUsername()).orElseThrow(() -> {
            throw new UserException(HttpStatus.BAD_REQUEST, new UserError(CodesError.VERIFY_INFO.getCode(), CodesError.VERIFY_INFO.getMessage()));
        });

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        user.setLastDate( now.format(format));

        if(user.getPassword().equals(loginDTO.getPassword())){
            userRepository.save(user);
            Map<String, String> claims = new HashMap<>();
            claims.put("username", user.getUsername());
            claims.put("roleId", user.getRole().getRoleId().toString());
            return new TokenDTO(JWTParser.createJWT(user.getUsername(), user.getUsername(), user.getUsername(), claims, 20* TokenTime.ONE_MINUTE.getTime()));
        }
        throw new UserException(HttpStatus.BAD_REQUEST, new UserError(CodesError.INVALID_CREDENTIALS.getCode(), CodesError.INVALID_CREDENTIALS.getMessage()));
    }

    @Override
    public User createUser(User user, UUID roleId) {
        userRepository.findById(user.getUsername()).ifPresent(userFound -> {
            throw new UserException(HttpStatus.BAD_REQUEST, new UserError(CodesError.INVALID_USERNAME.getCode(), CodesError.INVALID_USERNAME.getMessage()));
        });

        Role roleFound = roleRepository.findById(roleId).orElseThrow(() -> {
            throw new UserException(HttpStatus.BAD_REQUEST, new UserError(CodesError.ROLE_NOT_FOUND.getCode(), CodesError.ROLE_NOT_FOUND.getMessage()));
        });

        user.setRole(roleFound);

        User user1 = userRepository.save(user);
        System.out.println(user1);
        return user1;
    }
}
