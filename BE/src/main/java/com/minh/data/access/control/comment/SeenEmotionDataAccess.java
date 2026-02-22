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
    public final CurrentRepos r;

    public SeenEmotionDataAccess(CurrentRepos repos) {
        this.r = repos;
    }

    public GetEmotionResponse seenEmotion(Long id, Long userId) {
        ViewEmotionId compositeId = new ViewEmotionId(id, userId);

        r.viewEmotionRepository.save(
                new ViewEmotion(compositeId, LocalDateTime.now())
        );  // upsert

        Comment comment = r.commentRepository.getReferenceById(id);

        return new GetEmotionResponse(comment.getEmotion(), comment.getIsDebateClaim());
    }
}