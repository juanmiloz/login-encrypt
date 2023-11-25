package com.cibersecurity.login.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Table(name = "user_role")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID roleId;

    @Column(name = "role_name")
    private String roleName;

    private String description;


    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private List<Permission> rolePermission;

}
