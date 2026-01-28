package com.minh.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private byte[] password;

    public User(String name, byte[] password) {
        this.name = name;
        this.password = password;
    }

    public User() {

    }
}

