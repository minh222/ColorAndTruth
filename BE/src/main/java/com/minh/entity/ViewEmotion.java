package com.minh.entity;

import com.minh.entity.composite.id.ViewEmotionId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "view_emotion")
@Getter
@NoArgsConstructor
public class ViewEmotion {
    @EmbeddedId
    ViewEmotionId id;

    @Column
    @LastModifiedDate
    private LocalDateTime updateAt;

    public ViewEmotion(ViewEmotionId id, LocalDateTime updateAt) {
        this.id = id;
        this.updateAt = updateAt;
    }
}
