package com.minh.entity.id;

import lombok.EqualsAndHashCode;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class CompositeId implements Serializable {

    private Long commentId;

    private Long viewerId;

    public CompositeId(Long commentId, Long viewerId) {
        this.commentId = commentId;
        this.viewerId = viewerId;
    }

    public CompositeId() {

    }
}
