package com.minh.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.minh.auth.Verifier;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

import static com.minh.auth.Jwt.issue;
import static com.minh.auth.Verifier.verify;
import static com.minh.config.Config.TODAY;


@Entity
@Table(name = "user")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    @JsonIgnore
    private byte[] password;

    @Column
    private String avatar;

    @Column
    private Integer avatarChangeCount;

    @Column
    private LocalDate avatarChangeDate;

    public User(String name, byte[] password) {
        this.name = name;
        this.password = password;
    }

    public User() {

    }

    public void resetCountToday() {
        if (!Objects.equals(TODAY, avatarChangeDate)) { // today != (null or next day)
            avatarChangeCount = 0;
            avatarChangeDate = TODAY;
        }
    }

    public boolean exceed() {
        return avatarChangeCount >= 20;
    }

    public void emptyAvatarAndIncreaseCounter() {
        avatar = null;
        avatarChangeCount++;
    }

    public boolean isValidPassword(String tryPassword) {
        return verify(tryPassword.toCharArray(), password);
    }

    public String getToken() {
        return issue(id.toString(), 10000);
    }
}

