package com.ledungcobra.user.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role")
@Getter
@Setter
public class AppRole implements GrantedAuthority {
    @Id
    @Column(name = "id")
    private Integer id;


    @Column(name = "name")
    protected String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
