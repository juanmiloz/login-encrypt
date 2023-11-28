package com.cibersecurity.login.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {

    public String userName;
    public boolean isAdmin;
    public String validTru;

}
