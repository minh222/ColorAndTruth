package com.minh.data.access.control;

import com.minh.entity.Comment;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoadCommentCommentDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos repos;

    public LoadCommentCommentDataAccess(CurrentRepos repos) {
        this.repos = repos;
    }

    public List<Comment> loadComment(Long lastId, int limit) { // load từ lastId -> lastId + limit
        if (lastId == null) {
            lastId = repos.commentRepository.findMaxId() + 1;
        }

        return repos.commentRepository.findNext(lastId, PageRequest.of(0, limit));
    }

    public String getEmotionById(Long lastId) {
        return repos.commentRepository.getEmotionById(lastId);
    }

    public List<Comment> loadChildrenById(Long id, Long lastId, int limit) {
        if (lastId == null) {
            Long maxId = repos.commentRepository.findMaxIdByParentId(id);
            lastId = maxId == null ? null : maxId + 1;
        }

        return repos.commentRepository.findNextByParentId(id, lastId, PageRequest.of(0, limit));
    }
}