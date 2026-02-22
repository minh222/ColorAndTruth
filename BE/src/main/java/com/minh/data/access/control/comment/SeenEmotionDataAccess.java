package com.minh.data.access.control.comment;

import com.minh.config.DataAccess;
import com.minh.controller.comment.response.GetEmotionResponse;
import com.minh.data.access.control.CurrentRepos;
import com.minh.entity.Comment;
import com.minh.entity.ViewEmotion;
import com.minh.entity.composite.id.ViewEmotionId;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@DataAccess
public class SeenEmotionDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos rp;

    public SeenEmotionDataAccess(CurrentRepos repos) {
        this.rp = repos;
    }

    public GetEmotionResponse seenEmotion(Long id, Long userId) {
        rp.viewEmotionRp.save(
                new ViewEmotion(id, userId, LocalDateTime.now())
        );  // upsert

        Comment comment = rp.commentRp.getReferenceById(id);

        return new GetEmotionResponse(comment.getEmotion(), comment.getIsDebateClaim());
    }
}