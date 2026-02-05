package com.minh.data.access.control.comment;

import com.minh.data.access.control.CurrentRepos;
import com.minh.entity.id.CompositeId;
import com.minh.entity.ViewEmotion;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GetEmotionByIdDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos r;

    public GetEmotionByIdDataAccess(CurrentRepos repos) {
        this.r = repos;
    }

    public String getEmotionById(Long id, Long userId) {
        CompositeId compositeId = new CompositeId(id, userId);

        r.viewEmotionRepository.save(
                new ViewEmotion(compositeId, LocalDateTime.now())
        );  // upsert

        return r.commentRepository.getEmotionById(id);
    }
}