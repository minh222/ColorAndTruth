package com.minh.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

import static com.minh.config.Config.today;

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
        if (!Objects.equals(today, this.avatarChangeDate)) { // today != (null or next day)
            this.avatarChangeCount = 0;
            this.avatarChangeDate = today;
        }
    }

    public void emptyAvatarAndIncreaseCounter() {
        this.avatar = null;
        this.avatarChangeCount ++;
    }
}

