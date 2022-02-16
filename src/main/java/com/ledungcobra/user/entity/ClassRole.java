package com.ledungcobra.user.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "classrole")
@Entity
@Getter
@Setter
public class ClassRole {
    @Id
    @Column(name = "id")
    private Integer id;


    @Column(name = "name")
    protected String name;
}
