package com.minh.entity.composite.id;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
public class ReadNotifyId implements Serializable {
    private Long userId;
    private Long replyId;

    public ReadNotifyId(Long userId, Long replyId) {
        this.userId = userId;
        this.replyId = replyId;
    }

    public ReadNotifyId() {

    }
}
