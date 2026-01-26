package com.minh.data.access.control;

import com.minh.entity.Comment;
import com.minh.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataAccessService { // gateway :mỗi bussiness truy cập 1 cổng.
    @Autowired CurrentRepos repos;

    public Comment getComment(Long id) {
        return repos.commentRepository.findById(id);
    }

    public User getUser(Long id) {
        return repos.userRepository.findById(id);
    }
}