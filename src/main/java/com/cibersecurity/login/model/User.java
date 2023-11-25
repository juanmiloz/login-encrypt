package com.cibersecurity.login.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.hibernate.annotations.Type;

@Data
@Table(name = "`user`")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Type(type="org.hibernate.type.StringType")
    private String username;

    private String password;

    @Column(name = "last_connection")
    private String lastDate;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
