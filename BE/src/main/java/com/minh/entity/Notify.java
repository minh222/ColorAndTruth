package com.minh.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
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
@Table(name = "notify")
@Getter
@Setter
@AllArgsConstructor
public class Notify {
    @Id
    private Long fromId;

    @Column
    private Long toId;


    public Notify() {

    }
}

