package com.minh.data.access.control.comment;

import com.minh.data.access.control.CurrentRepos;
import com.minh.entity.Comment;
import org.springframework.stereotype.Service;

@Service
public class PostCommentDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos repos;

    public PostCommentDataAccess(CurrentRepos repos) {
        this.repos = repos;
    }

    public void saveComment(Long userId, String emotion, String claim, Long id, Boolean isDebateClaim) {
        repos.commentRepository.save(new Comment(userId, emotion, claim, id, isDebateClaim));
    }
}