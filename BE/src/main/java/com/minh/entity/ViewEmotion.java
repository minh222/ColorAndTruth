package com.minh.entity;

import com.minh.entity.id.CompositeId;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "view_emotion")
@Getter
public class ViewEmotion {
    @EmbeddedId
    CompositeId id;

    @Column
    @LastModifiedDate
    private LocalDateTime updateAt;

    public ViewEmotion(CompositeId id, LocalDateTime updateAt) {
        this.id = id;
        this.updateAt = updateAt;
    }

    public ViewEmotion() {

    }
}
