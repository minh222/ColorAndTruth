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

    private Integer viewerId;

    public CompositeId(Long commentId, Integer viewerId) {
        this.commentId = commentId;
        this.viewerId = viewerId;
    }

    public CompositeId() {

    }

    public boolean viewerIsNull() {
        return viewerId == null;
    }
}
