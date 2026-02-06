package com.minh.entity.id;

import lombok.EqualsAndHashCode;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class CompositeId implements Serializable {

    private Long commentId;

    private Integer viewerId;

    public CompositeId(Long commentId, Integer viewerId) {
        this.commentId = commentId;
        this.viewerId = viewerId;
    }

    public CompositeId() {

    }
}
