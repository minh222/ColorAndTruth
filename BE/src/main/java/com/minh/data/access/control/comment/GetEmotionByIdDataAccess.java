package com.minh.data.access.control.comment;

import com.minh.data.access.control.CurrentRepos;
import com.minh.entity.CompositeId;
import com.minh.entity.ViewEmotion;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GetEmotionByIdDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos repos;

    public GetEmotionByIdDataAccess(CurrentRepos repos) {
        this.repos = repos;
    }

    public String getEmotionById(Long id, Long userId) {
        CompositeId compositeId = new CompositeId(id, userId);

        repos.viewEmotionRepository.save(
                new ViewEmotion(compositeId, LocalDateTime.now())
        );  // upsert

        return repos.commentRepository.getEmotionById(id);
    }
}