package com.minh.data.access.control;

import com.minh.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostCommentDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    @Autowired CurrentRepos repos;

    public Comment getComment(Long id) {
        return repos.commentRepository.findById(id);
    }

    public Comment saveComment(String emotion, String claim) {
        return repos.commentRepository.save(new Comment(1L, emotion, claim));
    }
}