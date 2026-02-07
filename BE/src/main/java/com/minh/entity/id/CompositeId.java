package com.minh.entity.id;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.List;

@Embeddable
@EqualsAndHashCode
@Getter
public class CompositeId implements Serializable {

    private Long commentId;

    private Long viewerId;

    public CompositeId(Long commentId, Long viewerId) {
        this.commentId = commentId;
        this.viewerId = viewerId;
    }

    public CompositeId() {

    }

    public boolean viewerIsNull() {
        return viewerId == null;
    }
}
