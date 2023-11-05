package com.cibersecurity.login.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "`configuration`")
@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {

    @Id
    @Type(type="org.hibernate.type.StringType")
    private String name;

    private String value;

}
