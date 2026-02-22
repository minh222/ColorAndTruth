package com.minh.entity.composite.id;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.List;

@Embeddable
@EqualsAndHashCode
@Getter
public class ViewEmotionId implements Serializable {

    private Long commentId;

    private Long viewerId;

    public ViewEmotionId(Long commentId, Integer viewerId) {
        this.commentId = commentId;
        this.viewerId = Long.valueOf(viewerId);
    }

    public ViewEmotionId(Long commentId, Long viewerId) {
        this.commentId = commentId;
        this.viewerId = viewerId;
    }

    public ViewEmotionId() {

    }

    public boolean viewerIsNull() {
        return viewerId == null;
    }
}
