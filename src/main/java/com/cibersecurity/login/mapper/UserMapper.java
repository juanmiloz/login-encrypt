package com.cibersecurity.login.mapper;

import com.cibersecurity.login.dto.UpdateUserDTO;
import com.cibersecurity.login.dto.UserDTO;
import com.cibersecurity.login.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User fromDTO (UserDTO userDTO);

    User fromUpdateUser(UpdateUserDTO updateUserDTO);

    @Mapping(source = "role.roleId", target = "roleId")
    UserDTO fromUser(User user);

}
