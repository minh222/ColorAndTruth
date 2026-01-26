package com.minh.entity;

import javax.persistence.*;

@Entity
@Table(name = "comment")
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

    public Comment(Long userId, String emotion, String claim) {

        this.userId = userId;
        this.emotion = emotion;
        this.claim = claim;
    }

    public Comment() {

    }
}
