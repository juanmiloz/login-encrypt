package com.cibersecurity.login.api;

import com.cibersecurity.login.dto.UpdateUserDTO;
import com.cibersecurity.login.dto.UserDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/user")
public interface UserApi {

    @GetMapping
    List<UserDTO> getUsers(@RequestParam @Valid String token);

    @DeleteMapping({"/{username}"})
    UserDTO deleteUser(@RequestParam @Valid String token, @RequestParam @Valid String username);

    @PutMapping()
    UserDTO updateUser(@RequestParam @Valid String token, @Valid @RequestBody UpdateUserDTO username);
}
