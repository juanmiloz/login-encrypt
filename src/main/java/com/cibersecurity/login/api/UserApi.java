package com.cibersecurity.login.api;

import com.cibersecurity.login.dto.UpdateUserDTO;
import com.cibersecurity.login.dto.UserDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/user")
public interface UserApi {

    @GetMapping
    List<UserDTO> getUsers();

    @DeleteMapping({"/{username}"})
    UserDTO deleteUser(@PathVariable String username);

    @PutMapping()
    UserDTO updateUser(@Valid @RequestBody UpdateUserDTO username);
}
