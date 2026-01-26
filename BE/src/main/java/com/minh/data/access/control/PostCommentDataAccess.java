package com.minh.data.access.control;

import com.minh.entity.Comment;
import org.springframework.stereotype.Service;

@Service
public class PostCommentDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos repos;

    public PostCommentDataAccess(CurrentRepos repos) {
        this.repos = repos;
    }

    public Comment saveComment(String emotion, String claim) {
        return repos.commentRepository.save(new Comment(1L, emotion, claim));
    }
}