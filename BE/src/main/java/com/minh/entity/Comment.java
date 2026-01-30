package com.minh.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "comment")
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @Column
    private String emotion;

    @Column
    private String claim;

    @Column
    private Long parentId;

    @Column
    private Boolean isDebateClaim;

    public Comment(Long userId, String emotion, String claim,Long parentId, Boolean isDebateClaim) {

        this.userId = userId;
        this.emotion = emotion;
        this.claim = claim;
        this.parentId = parentId;
        this.isDebateClaim = isDebateClaim;
    }

    public Comment() {

    }
}
