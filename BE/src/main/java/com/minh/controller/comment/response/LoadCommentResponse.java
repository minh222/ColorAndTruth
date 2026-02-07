package com.minh.controller.comment.response;

import com.minh.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;


@Getter

public class LoadCommentResponse {
    private Long id;
    private String emotion;
    private String claim;
    private String name;
    private String avatar;
    private Long count;
    private Long userId;
    private LocalDateTime time;
    private Boolean isDebateClaim;
    private Long viewerId;

    public Boolean getIsDebateClaim() {
        return viewerId == null || !Objects.equals(isDebateClaim, false);
    }

    public String getEmotion() {
        if (emotion != null) {
            return "0";
        }
        return null;
    }

    public LoadCommentResponse(Long id, String emotion, String claim, String name, String avatar, Long count, Long userId, LocalDateTime time, Boolean isDebateClaim, Long viewerId) {
        this.id = id;
        this.emotion = emotion;
        this.claim = claim;
        this.name = name;
        this.avatar = avatar;
        this.count = count;
        this.userId = userId;
        this.time = time;
        this.isDebateClaim = isDebateClaim;
        this.viewerId = viewerId;
    }

    public Long getCount() {
        return count - 1;
    }
}
