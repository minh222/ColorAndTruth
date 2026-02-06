package com.minh.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
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

    @Column
    private LocalDate date;

    @Column
    private Integer count;

    @Column
    private LocalDateTime time;

    public Comment(Long userId, String emotion, String claim,Long parentId, Boolean isDebateClaim, Integer count, LocalDate date, LocalDateTime time) {
        this.userId = userId;
        this.emotion = emotion;
        this.claim = claim;
        this.parentId = parentId;
        this.isDebateClaim = isDebateClaim;
        this.count = count;
        this.date = date;
        this.time = time;
    }

    public Boolean getDebateClaim(Long userId) {
        return isDebateClaim == null || userId == null;
    }
}
