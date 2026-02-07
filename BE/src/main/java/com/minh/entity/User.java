package com.minh.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

import static com.minh.auth.Jwt.issue;
import static com.minh.auth.Security.verify;
import static com.minh.config.Config.TODAY;


@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // ignore proxy
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

    public boolean verifying(String tryPassword) {
        return verify(tryPassword.toCharArray(), password);
    }

    public String createToken() {
        return issue(id.toString(), 100000);
    }
}

