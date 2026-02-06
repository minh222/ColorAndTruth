package com.minh.data.access.control.comment;

import com.minh.controller.comment.response.GetEmotionResponse;
import com.minh.data.access.control.CurrentRepos;
import com.minh.entity.Comment;
import com.minh.entity.id.CompositeId;
import com.minh.entity.ViewEmotion;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class SeenEmotionDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos r;

    public SeenEmotionDataAccess(CurrentRepos repos) {
        this.r = repos;
    }

    public GetEmotionResponse seenEmotion(Long id, Long userId) {
        CompositeId compositeId = new CompositeId(id, Math.toIntExact(userId));

        r.viewEmotionRepository.save(
                new ViewEmotion(compositeId, LocalDateTime.now())
        );  // upsert

        Comment comment = r.commentRepository.getReferenceById(id);

        return new GetEmotionResponse(comment.getEmotion(), comment.getIsDebateClaim());
    }
}